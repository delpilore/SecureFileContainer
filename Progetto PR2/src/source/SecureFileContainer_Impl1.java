package source;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

public class SecureFileContainer_Impl1<E> implements SecureFileContainer<E> {
	
    // AF(c) = { < Id, c.security.get(Id), <c.data.get(Id)> > | c.security.containsKey(Id) }
	
    // IR = (c.security != null) && (c.data != null)
    //		&& (c.data.containsKey(Id) <=> c.security.containsKey(Id))
	//		&& (a = c.security.keySet(), for all i,j. 0 <= i,j < a.size(), a(i) != a(j))
	//		&& (c.security.containsKey(Id) <=> c.security.get(Id) != null)
	//		&& (c.data.containsKey(Id) <=> c.data.get(Id) != null)

	private HashMap<String, String> security; 				// Struttura dati per il controllo degli accessi ai dati, attraverso l'associazione utente password
	private HashMap<String, ArrayList<File<E>>> data; 		// Struttura dati per il salvataggio dei file relativi ad un utente (previo accesso controllato con "security")
															// L'array che conterrà i file, conterrà oggetti di tipo astratto File<E> (interfaccia e implementazione nei file specifici)
	
	// Metodo costruttore che inizializza le due strutture dati 
	public SecureFileContainer_Impl1() {
		security = new HashMap<String, String>(0);
		data = new HashMap<String, ArrayList<File<E>>>(0);	
	}
	/*
	 * Il metodo costruttore preserva l'IR, perchè l'unica cosa che fa è inizializzare c.security e c.data, in modo che siano entrambi != null
	 */

	// Crea l’identità di un nuovo utente della collezione
	public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyRegisteredException, WeakPasswordException, IllegalUsernameException {
		
		// Se sono stati passati uno dei due, o entrambi, gli argomenti null, lancio un'eccezione
		if (Id==null || passw==null)
			throw new NullPointerException();
		
		// Se security contiene già la key "Id" vuol dire che l'utente era già registrato e lancio quindi un'eccezione (non ammetto username duplicati, come da IR)
		if (security.containsKey(Id))
			throw new UserAlreadyRegisteredException(Id + " è già registrato!");
		
		// Se la passw passata come argomento ha lunghezza < 5 lancio un'eccezione per password debole
		if (passw.length() < 5)
			throw new WeakPasswordException("La tua password è troppo corta!");
		
		// Se l'Id passato ha lunghezza < 2 lancio un'eccezione per nome utente non ammesso
		if (Id.length() < 2)
			throw new IllegalUsernameException("Il tuo username è troppo corto!");
		
	    security.put(Id, passw); 							// Aggiungo alla struttura dati per il controllo degli accessi futuri, l'associazione tra l'username Id e la password passw
        data.put(Id, new ArrayList<File<E>>(0)); 			// Aggiungo alla struttura dati per il salvataggio dei file, l'associazione tra l'username Id e un oggetto ArrayList inizializzato a 0 e istanziato per contenere File<E>
	}
	/*
	 * Questo metodo preserva l'IR perchè non permette l'inserimento di un utente già presente (non ammette quindi duplicati nelle key, come da IR)
	 * Inserisce la key Id sia in c.security (insieme alla passw) che in c.data e istanzia c.data.get(Id) in modo che sia != null, come da IR.
	 */

	// Restituisce il numero dei file di un utente presenti nella collezione
	public int getSize(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		// Se sono stati passati uno dei due, o entrambi, gli argomenti null, lancio un'eccezione
		if (Owner==null || passw==null)
			throw new NullPointerException();
		
		// Se l'utente Owner non è tra le key presenti in security, vuol dire che non si è registrato e non ha quindi accesso a nessuna collezione di file, lancio un'eccezione
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		// Se l'utente Owner è presente tra le key in security, ma la passw passata come argomento non corrisponde a quella salvata in security, lancio un'eccezione
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		// Restituisco la size() dell'ArrayList di File<E> relativo al utente Owner
		return data.get(Owner).size();						
		
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */

	// Inserisce il il file nella collezione se vengono rispettati i controlli di identità
	public boolean put(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		// Se sono stati passati uno dei due, o entrambi, gli argomenti null, lancio un'eccezione
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		// Se l'utente Owner non è tra le key presenti in security, vuol dire che non si è registrato e non ha quindi accesso a nessuna collezione di file, lancio un'eccezione
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		// Se l'utente Owner è presente tra le key in security, ma la passw passata come argomento non corrisponde a quella salvata in security, lancio un'eccezione
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		// Aggiungo ai files di Owner, che ha passato il controllo di sicurezza, l'oggetto MyFile<E> (di tipo File<E>) che incapsula l'argomento <E file> passato, 
		// insieme ad altre informazioni, quali: il nome del possessore del file, i nomi di coloro che hanno l'accesso in lettura al seguente file e i nomi di coloro 
		// che hanno l'accesso anche in scrittura. Inizialmente, questi ultimi due campi saranno abitati solo da Owner, che potrà in futuro scegliere altri utenti
		// a cui dare i permessi.
		data.get(Owner).add(new MyFile<E>(Owner, file));

		// Ritorno true ad aggiunta effettuata, non ritorno mai false perchè sempre preceduto da un'eccezione
		return true;
		
	}
	/*
	 * Questo metodo preserva l'IR visto che va semplicemente ad aggiungere un file a c.data.get(Owner), se quest'ultimo risulta registrato correttamente al sistema.
	 * security.containsKey(Owner) && data.containsKey(Owner) 
	 */

	// Ottiene una copia del file nella collezione se vengono rispettati i controlli di identità
	public E get(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {

		// Se sono stati passati uno dei due, o entrambi, gli argomenti null, lancio un'eccezione
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		// Se l'utente Owner non è tra le key presenti in security, vuol dire che non si è registrato e non ha quindi accesso a nessuna collezione di file, lancio un'eccezione
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		// Se l'utente Owner è presente tra le key in security, ma la passw passata come argomento non corrisponde a quella salvata in security, lancio un'eccezione
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		// Se l'utente Owner ha passato il controllo accessi di security, ma il file E che vuole ottenere non esiste nella sua collezione, lancio un'eccezione
		if (data.get(Owner).size()==0)
			throw new NoDataException("Il tuo file storage è vuoto!");
		
		// Prendo l'ArrayList di File<E> relativo a Owner e lo comincio a scorrere
		ArrayList<File<E>> files_temp = data.get(Owner);	
		
		E temp =  files_temp.get(0).getData();		 		// getData() serve per estrarre dal File<E> il dato E, lo copio in Shallow Copy	
		int i=1;
		
		while(!temp.equals(file) && i<files_temp.size()){	// Ciclo finchè non trovo il file richiesto da Owner
			temp = files_temp.get(i).getData();
			i++;
		}
		
		if (temp!=file)				
			// Se il file non è stato trovato lancio un'eccezione
			throw new NoDataException("Il file da te richiesto non esiste nel file storage!"); 
		else {
			// Se il file E è stato trovato, procedo con il controllare chi è il vero proprietario del file. 
			// In caso non sia Owner, controllo che tipo di accesso gli è stato fornito, chi è il proprietario originale e infine, ritorno il file E.
			// Altrimenti, in caso il proprietario sia proprio Owner, restituisco il file E comunicando la possibilità di accesso in lettura/scrittura.
			File<E> check = files_temp.get(i-1);
			if(!check.getOwner().equals(Owner)) {			// Se Owner non è il vero proprietario del file		
				if(check.isSharedR(Owner))					// Controllo i permessi d'accesso forniti ad Owner dal vero proprietario
					System.out.println("[Download] Il file: <" + file + "> che " + Owner + " sta per scaricare, è stato condiviso da " + check.getOwner() + " in sola lettura!");
				else
					System.out.println("[Download] Il file: <" + file + "> che " + Owner + " sta per scaricare, è stato condiviso da " + check.getOwner() + " in lettura e scrittura!");
			}
			else 
				System.out.println("[Download] Il file: <" + file + "> che stai per scaricare, è di tua proprietà, con accesso in scrittura e lettura");
			
			return temp;
		}
	}
	/*
	 * Questo metodo preserva l'IR perchè non va a modificare nulla, si limita a ritornare, se trovato, una copia del file cercato dall'utente Owner 
	 */


	public E remove(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		if (data.get(Owner).size()==0)
			throw new NoDataException("Il tuo file storage è vuoto!");
		
		ArrayList<File<E>> files_temp = data.get(Owner);	// Prendo l'ArrayList di File<E> relativo a Owner e lo comincio a scorrere
		
		E temp =  files_temp.get(0).getData();
		int i=1;
		
		while(!temp.equals(file) && i<files_temp.size()){
			temp = files_temp.get(i).getData();
			i++;
		}
		
		
		if (temp!=file)
			throw new NoDataException("Il file che vuoi rimuovere non esiste nel file storage!");
		else
			files_temp.remove(files_temp.get(i-1));
		
		return file;
	}


	public void copy(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		if (data.get(Owner).size()==0)
			throw new NoDataException("Il tuo file storage è vuoto!");
		
		ArrayList<File<E>> files_temp = data.get(Owner);
		
		E temp =  files_temp.get(0).getData();
		int i=1;
		
		while(!temp.equals(file) && i<files_temp.size()){
			temp = files_temp.get(i).getData();
			i++;
		}
		
		
		if (temp!=file)
			throw new NoDataException("Il file che vuoi copiare non esiste nel file storage!");
		else 
			this.put(Owner, passw, file);
		
	}


	public void shareR(String Owner, String passw, String Other, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.containsKey(Other))
			throw new UserNotFoundException("L'utente con cui vuoi condividere il file in sola lettura, non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		if (data.get(Owner).size()==0)
			throw new NoDataException("Il tuo file storage è vuoto!");
		
		ArrayList<File<E>> files_temp = data.get(Owner);
		
		E temp =  files_temp.get(0).getData();
		int i=1;
		
		while(!temp.equals(file) && i<files_temp.size()){
			temp = files_temp.get(i).getData();
			i++;
		}
		
		
		if (temp!=file)
			throw new NoDataException("Il file che vuoi condividere in lettura, non esiste nel file storage!");
		else {
			files_temp.get(i-1).setShareR(Other);
			data.get(Other).add(files_temp.get(i-1));
		}
	}


	public void shareW(String Owner, String passw, String Other, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.containsKey(Other))
			throw new UserNotFoundException("L'utente con cui vuoi condividere il file in sola lettura, non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		if (data.get(Owner).size()==0)
			throw new NoDataException("Il tuo file storage è vuoto!");
		
		E temp =  data.get(Owner).get(0).getData();
		int i=1;
		
		while(!temp.equals(file) && i<data.get(Owner).size()){
			temp = data.get(Owner).get(i).getData();
			i++;
		}
		
		
		if (temp!=file)
			throw new NoDataException("Il file che vuoi condividere in scrittura, non esiste nel file storage!");
		else {
			data.get(Owner).get(i-1).setShareW(Other);
			data.get(Other).add(data.get(Owner).get(i-1));
		}
		
	}

	public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		if (Owner==null || passw==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		ArrayList<E> temp = new ArrayList<E>();
		
		for (int i=0; i<data.get(Owner).size(); i++){
			  File<E> p = data.get(Owner).get(i);
			  temp.add(p.getData());
		}
		
		Iterator<E> it = temp.iterator();
		return it;
	}


}
