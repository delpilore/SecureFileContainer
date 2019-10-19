package source;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

// AUTHOR: Lorenzo Del Prete, Corso B, 531417

// IMPORTANTE: Questa implementazione di SecureFileContainer<E> utilizza in larga parte il tipo File<E>, creato da me come supporto alle operazione del file storage.
//			   Per una lettura più chiara del codice seguente consultare la relazione, o i file File.java e MyFile.java.

public class SecureFileContainer_Impl1<E> implements SecureFileContainer<E> {
	
    // AF(c) = { < Id, c.security.get(Id), <c.data.get(Id)> > | c.security.containsKey(Id) }
	
    // IR = (c.security != null) && (c.data != null)
    //		&& (c.data.containsKey(Id) <=> c.security.containsKey(Id))
	//		&& (a = c.security.keySet(), for all i,j. 0 <= i,j < a.size(), i!=j => a(i) != a(j))
	//		&& (c.security.containsKey(Id) <=> c.security.get(Id) != null)
	//		&& (c.data.containsKey(Id) <=> c.data.get(Id) != null)
	
	private HashMap<String, String> security; 				// Struttura dati per il controllo degli accessi ai dati, attraverso l'associazione <utente, password>
	
	private HashMap<String, ArrayList<File<E>>> data; 		// Struttura dati per il salvataggio dei file, attraverso l'associazione <utente, <files>> (previo accesso controllato con "security").
															// L'array che conterrà i files, conterrà oggetti di tipo astratto File<E>. 
	
	// Metodo costruttore che inizializza le due strutture dati 
	public SecureFileContainer_Impl1() {
		security = new HashMap<String, String>(0);
		data = new HashMap<String, ArrayList<File<E>>>(0);	
	}
	/*
	 * Il metodo costruttore preserva l'IR, perchè l'unica cosa che fa è inizializzare c.security e c.data, in modo che siano entrambe != null
	 */

	// Crea l’identità di un nuovo utente della collezione
	public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyRegisteredException, WeakPasswordException, IllegalUsernameException {
		
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Id==null || passw==null)
			throw new NullPointerException();
		
		if (security.containsKey(Id))
			throw new UserAlreadyRegisteredException();
		
		if (passw.length() < 5)
			throw new WeakPasswordException();

		if (Id.length() < 2)
			throw new IllegalUsernameException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
	    security.put(Id, passw); 							// Aggiungo alla struttura dati per il controllo degli accessi futuri, l'associazione tra l'username Id e la password passw
        data.put(Id, new ArrayList<File<E>>(0)); 			// Aggiungo alla struttura dati per il salvataggio dei file, l'associazione tra l'username Id e un oggetto ArrayList istanziato per contenere File<E>
	}
	/*
	 * Questo metodo preserva l'IR perchè non permette l'inserimento di un utente già presente (non ammette quindi duplicati nelle key, come da IR)
	 * Inserisce la key Id sia in c.security che in c.data e istanzia c.data.get(Id) in modo che sia != null, come da IR.
	 */

	// Restituisce il numero dei file di un utente presenti nella collezione
	public int getSize(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Owner==null || passw==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException();

		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Restituisco la size() dell'ArrayList di File<E> relativo al utente Owner		
		return data.get(Owner).size();									
		
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */

	// Inserisce il file nella collezione se vengono rispettati i controlli di identità
	public boolean put(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException();

		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Aggiungo ai files di Owner, che ha passato il controllo di sicurezza, l'oggetto MyFile<E> (di tipo astratto File<E>) che incapsula, tra le altre cose, l'argomento E file passato.
		data.get(Owner).add(new MyFile<E>(Owner, file));

		// Ritorno true ad aggiunta effettuata, non ritorno mai false perchè sempre preceduto da un'eccezione
		return true;
		
	}
	/*
	 * Questo metodo preserva l'IR visto che va semplicemente ad aggiungere un file a c.data.get(Owner), se quest'ultimo risulta registrato correttamente al sistema.
	 * Non rischia di portare nulla a null e non rompe la condizione di non duplicità delle key.
	 */

	// Ottiene una copia del file nella collezione se vengono rispettati i controlli di identità
	public E get(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		
		//-----CONTROLLI PRELIMINARI-----//

		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException();
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException();
		
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Il metodo searchFor è un metodo privato, proprio di questa classe. (scendere al metodo in questione per i commenti sul suo funzionamento)
		File<E> check = this.searchFor(Owner, file, null);

		if (check==null)				
			// Se il file non è stato trovato lancio un'eccezione
			throw new NoDataException(); 
		else {
			// Se il file E è stato trovato, procedo con il controllare chi è il vero proprietario del file. 
			// In caso non sia Owner, controllo che tipo di accesso gli è stato fornito, chi è il proprietario originale e infine, ritorno il file E.
			// Altrimenti, in caso il proprietario sia proprio Owner, restituisco il file E comunicando la possibilità di accesso in lettura/scrittura.
			// getOwner() è un metodo pubblico fornito da File<E>, si limita a restituire il nome del proprietario originale del file.
			if(!check.getOwner().equals(Owner)) {	
				// Controllo i permessi d'accesso forniti ad Owner dal vero proprietario con isSharedR che è un metodo pubblico fornito da File<E>
				// Esso controlla se il nome di Owner è presente tra i nomi di utenti che hanno l'accesso in lettura al file in questione
				if(check.isSharedR(Owner))					
					System.out.println("[Download] Il file: <" + file + "> che " + Owner + " sta per scaricare, è stato condiviso da " + check.getOwner() + " in sola lettura!");
				else
					System.out.println("[Download] Il file: <" + file + "> che " + Owner + " sta per scaricare, è stato condiviso da " + check.getOwner() + " in lettura e scrittura!");
			}
			else 					
				// Owner è il proprietario legittimo e quindi può accedere sia in scrittura che lettura al file che sta scaricando
				System.out.println("[Download] Il file: <" + file + "> che stai per scaricare, è di tua proprietà, con accesso in scrittura e lettura");
			
			// Ritorno il file E
			return check.getData();
		}
	}
	/*
	 * Questo metodo preserva l'IR perchè non va a modificare nulla, si limita a ritornare, se trovato, una copia del file cercato dall'utente Owner 
	 */

	// Rimuove il file dalla collezione se vengono rispettati i controlli di identità
	public E remove(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
	
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException();
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException();
			
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		//----- FINE CONTROLLI PRELIMINARI-----//
		
		// Il metodo searchFor è un metodo privato, proprio di questa classe. (scendere al metodo in questione per i commenti sul suo funzionamento)
		File<E> check = this.searchFor(Owner, file, null);
		
		if (check==null)
			throw new NoDataException();
		else {
			
			if (!check.getOwner().equals(Owner)) // sto cercando di eliminare un file che mi è stato condiviso e che non è quindi di mia proprietà
				data.get(Owner).remove(check); // lo rimuovo semplicemente
			else {
				// Se sto invece cercando di rimuovere un file di mia proprietà, lo rimuovo anche a coloro a cui l'avevo condiviso
				File<E> shareremove;
				String temp;
				
				Iterator<String> it = check.getIteratorRead();
				while (it.hasNext()) {
					temp = it.next();
					shareremove = this.searchFor(temp, file, Owner);
					if(shareremove!=null)
						data.get(temp).remove(shareremove);
				}
				
				it = check.getIteratorWrite();
				while (it.hasNext()) {
					temp = it.next();
					shareremove = this.searchFor(temp, file, Owner);
					if(shareremove!=null)
						data.get(temp).remove(shareremove);
				}
			}
		}
		return file;
	}
	/*
	 * Questo metodo preserva l'IR perchè si limita a rimuovere un file da data.get(Owner) senza inficiare alcuna condizione posta dall'IR (non porta nulla
	 * a null e non spezza la condizione di non duplicità)
	 */

	// Crea una copia del file nella collezione se vengono rispettati i controlli di identità
	public void copy(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException();
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException();
		
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Il metodo searchFor è un metodo privato, proprio di questa classe. (scendere al metodo in questione per i commenti sul suo funzionamento)
		File<E> check = this.searchFor(Owner, file, null);
		
		if (check==null)
			throw new NoDataException();
		else 
			// Essere in questo ramo dell'if significa aver rintracciato il file E attraverso searchFor.
			// Per copiarlo semplicemente lo inserisco di nuovo in collezione attraverso il metodo put della classe SecureFileContainer stessa
			this.put(Owner, passw, file);
		
	}
	/*
	 * Questo metodo preserva l'IR perchè si limita a copiare un file di data.get(Owner), in data.get(Owner) stesso, senza inficiare alcuna condizione posta dall'IR (non porta nulla
	 * a null e non spezza la condizione di non duplicità).
	 * La condizione di non duplicità si applica alle key ma non ai dati contenuti in ogni ArrayList<File<E>> che invece ammettono duplicati.
	 */

	// Condivide in lettura il file nella collezione con un altro utente se vengono rispettati i controlli di identità
	public void shareR(String Owner, String passw, String Other, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException, IllegalSharingException {
		
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Owner==null || passw==null || Other==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException();
		
		if (!security.containsKey(Other))
			throw new UserNotFoundException();
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException();
		
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		//-----CONTROLLI PRELIMINARI-----//
		
		// Il metodo searchFor è un metodo privato, proprio di questa classe. (scendere al metodo in questione per i commenti sul suo funzionamento)
		File<E> check = this.searchFor(Owner, file, null);
		
		// Se Owner vuole inviare un file di cui non è proprietario lancio un'eccezione
		if (check!=null && !check.getOwner().equals(Owner))
			throw new IllegalSharingException();
		
		if (check==null)
			throw new NoDataException();
		else {
			
			// Se il file era già stato condiviso da Owner ad Other, lo rimuovo temporaneamente, per poi
			// riaggiungerlo aggiornato, con il nuovo permesso d'accesso
			if(this.searchFor(Other,file, Owner) != null)
				data.get(Other).remove(check);
					
			// Il metodo setShareR è un metodo pubblico fornito da File<E>, in poche parole fa in modo che il file check si "ricordi" di essere stato condiviso in lettura
			// all'utente Other (andare al metodo in questione per i commenti specifici sul suo funzionamento)
			check.setShareR(Other);
			
			// Aggiungo all'ArrayList<File<E>> proprio di Other, il file check
			data.get(Other).add(check);
		}
	}
	/*
	 * Questo metodo preserva l'IR perchè aggiunge semplicemente un file a data.get(Other) senza compromettere nessuna condizione dell'IR.
	 */

	// Condivide in lettura e scrittura il file nella collezione con un altro utente se vengono rispettati i controlli di identità
	public void shareW(String Owner, String passw, String Other, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException, IllegalSharingException {
		
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Owner==null || passw==null || Other==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException();
		
		if (!security.containsKey(Other))
			throw new UserNotFoundException();
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException();
		
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Il metodo searchFor è un metodo privato, proprio di questa classe. (scendere al metodo in questione per i commenti sul suo funzionamento)
		File<E> check = this.searchFor(Owner, file, null);
		
		// Se Owner vuole inviare un file di cui non è proprietario lancio un'eccezione
		if (check!=null && !check.getOwner().equals(Owner))
			throw new IllegalSharingException();
		
		if (check==null)
			throw new NoDataException();
		else {
			
			// Se il file era già stato condiviso da Owner ad Other, lo rimuovo temporaneamente, per poi
			// riaggiungerlo aggiornato, con il nuovo permesso d'accesso
			if(this.searchFor(Other,file, Owner) != null)
				data.get(Other).remove(check);
			
			// Il metodo setShareW è un metodo pubblico fornito da File<E>, in poche parole fa in modo che il file check si "ricordi" di essere stato condiviso in lettura e scrittura
			// all'utente Other (andare al metodo in questione per i commenti specifici sul suo funzionamento)
			check.setShareW(Other);
			
			// Aggiungo all'ArrayList<File<E>> proprio di Other, il file check
			data.get(Other).add(check);
		}
		
	}
	/*
	 * Questo metodo preserva l'IR perchè aggiunge semplicemente un file a data.get(Other) senza compromettere nessuna condizione dell'IR.
	 */
	
	// Restituisce un iteratore (senza remove) che genera tutti i file dell’utente in ordine arbitrario se vengono rispettati i controlli di identità
	public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Owner==null || passw==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException();
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException();
		
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Creo un ArrayList<E> da popolare
		ArrayList<E> temp = new ArrayList<E>();
		
	    // Popolo l'ArrayList<E> precedentemente istanziato, dei file E (incapsulati nei vari File<E>) relativi all'utente Owner
		for (int i=0; i<data.get(Owner).size(); i++){
			  File<E> p = data.get(Owner).get(i);
			  temp.add(p.getData());
		}
		
		// Costruisco l'iteratore sull'ArrayList<E> precedente e lo ritorno
		Iterator<E> it = temp.iterator();
		return it;
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */
	
	
	
	// Il compito di questo metodo è rintracciare il File<E>, che incapsula il file E originariamente ricercato (Comparison), dell'ArrayList<File<E>> (proprio di Owner)
	// Con Mode==False verrà semplicemente rintracciato il primo file uguale a quello ricercato.
	// Con Mode==True verrà rintracciato il primo file uguale a quello ricercato, E INOLTRE, non di proprietà di Owner, quindi un file condiviso.
	// Se il file ricercato non dovesse esistere, verrà ritornato null.
	private File<E> searchFor(String Owner, E Comparison, String realOwner) {
		
		if(data.get(Owner).size()==0)
			return null;

		ArrayList<File<E>> files_temp = data.get(Owner);	
		
		File<E> temp =  files_temp.get(0);		
		int i=1;
		
		if(realOwner == null) {
			// getData() è un metodo pubblico fornito da File<E>, permette di ottenere il file E incapsulato in esso
			while(!temp.getData().equals(Comparison) && i<files_temp.size()){	// Ciclo finchè non trovo il file richiesto da Owner
				temp = files_temp.get(i);
				i++;
			}
			
			if(temp.getData().equals(Comparison))								
				// Ritorno il File<E> che incapsula E Comparison, ovvero il file ricercato
				return temp;
			else
				return null;
		}
		else {
			while( (!temp.getData().equals(Comparison) || !temp.getOwner().equals(realOwner)) && i<files_temp.size()){	
				temp = files_temp.get(i);
				i++;
			}
			
			if(temp.getData().equals(Comparison) && temp.getOwner().equals(realOwner))
				return temp;
			else
				return null;
		}
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */
}
