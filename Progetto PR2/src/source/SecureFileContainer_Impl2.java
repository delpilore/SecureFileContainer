package source;

import java.util.ArrayList;
import java.util.Iterator;

// AUTHOR: Lorenzo Del Prete, Corso B, 531417

// IMPORTANTE: Questa implementazione di SecureFileContainer<E> utilizza in larga parte il tipo File<E>, creato da me come supporto alle operazione del file storage.
//			   Per una lettura più chiara del codice seguente consultare la relazione, o i file File.java e MyFile.java.

public class SecureFileContainer_Impl2<E> implements SecureFileContainer<E> {
	
    // AF(c) = { < c.security_user.get(i), c.security_pass.get(i), <c.data.get(i)> > | c.security_user.get(i)!=null }
	
    // IR = (c.security_user != null) && (c.security_pass != null) && (c.data != null) && (forall i. 0 <= i < c.data.size(), c.data.get(i) != null)
    //		&& (c.security_user.contains(Id) <=> c.security_pass.get(security_user.indexOf(Id))!= null <=> c.data.get(security_user.indexOf(Id)) != null)
	//		&& (for all i,j. 0 <= i,j < c.security_user.size(), i!=j => security_user.get(i) != security_user.get(j))
	
	private ArrayList<String> security_user;				// Struttura dati per il salvataggio degli username degli utenti del file storage
	private ArrayList<String> security_pass;				// Struttura dati per il salvataggio delle password degli utenti del file storage
	private ArrayList<ArrayList<File<E>>> data;				// Struttura dati per il salvataggio dei file relativi ad un utente (previo accesso 
															// controllato con security_user e security_pass).
															
	// Il principio su cui si basa questa implementazione è che gli ArrayList mantengono l'ordine d'inserimento.
	// All'atto della registrazione di un utente, verranno aggiunti, tramite il metodo add: l'username a security_user, la password a 
	// security_pass e un oggetto ArrayList istanziato per contenere File<E> a data.
	// Facendo queste 3 operazioni in successione e nello stesso metodo, avremo una relazione tra indici nei tre ArrayList.
	// Mettiamo per esempio di voler registrare pippo con password 12345:
	// - security_user.get(i) = pippo
	// - security_pass.get(i) = 12345
	// - data.get(i) = ArrayList<File<E>> (oggetto istanziato != null)
	// Attraverso l'indice i, è possibile accedere ad ogni campo che ci serve per gestire l'accesso controllato e il salvataggio dei file, dell'utente pippo.
	
	// Metodo costruttore che inizializza le 3 strutture dati
	public SecureFileContainer_Impl2() {
		security_user = new ArrayList<String>(0);
		security_pass = new ArrayList<String>(0);
		data = new ArrayList<ArrayList<File<E>>>(0);	
	}
	/*
	 * Il metodo costruttore preserva l'IR, perchè l'unica cosa che fa è inizializzare tutte le strutture dati in modo che siano != null
	 */

	// Crea l’identità di un nuovo utente della collezione	
	public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyRegisteredException, WeakPasswordException, IllegalUsernameException {
		
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Id==null || passw==null)
			throw new NullPointerException(); 
		
		if (security_user.contains(Id))
			throw new UserAlreadyRegisteredException();
		
		if (passw.length() < 5)
			throw new WeakPasswordException();

		if (Id.length() < 2)
			throw new IllegalUsernameException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		security_user.add(Id);						// Aggiungo a security_user l'username Id
		security_pass.add(passw);					// Aggiungo a security_pass la password passw
		data.add(new ArrayList<File<E>>(0));		// Aggiungo a data un oggetto ArrayList<File<E>>
		
		// Alla fine di queste 3 operazioni, troverò allo stesso indice per tutte e 3 le strutture dati,
		// i campi relativi all'utente registrato.
		}
	/*
	 * Questo metodo preserva l'IR perchè non permette l'inserimento di un utente già presente (non ammette quindi duplicati in security_user, come da IR)
	 * Inserisce l'username Id in security_user(i) e di conseguenza rende security_passw.get(i) != null, inserendoci la password relativa all'utente.
	 * Stesso vale per data.get(i) che diventa diverso da null, dopo l'inserimento di un oggetto istanziato ArrayList<File<E>>.
	 */

	// Restituisce il numero dei file di un utente presenti nella collezione
	public int getSize(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Owner==null || passw==null)
			throw new NullPointerException();
		
		if (!security_user.contains(Owner))
			throw new UserNotFoundException();

		if (!security_pass.get(security_user.indexOf(Owner)).equals(passw))
			throw new WrongPasswordException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Restituisco la size() dell'ArrayList di File<E> relativo al utente Owner.
		// Per rintracciare l'indice giusto, utilizzo sempre il concetto di correlazione tra indici, accennato precedentemente.
		return data.get(security_user.indexOf(Owner)).size();
		
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */

	// Inserisce il file nella collezione se vengono rispettati i controlli di identità
	public boolean put(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException {
	
		//-----CONTROLLI PRELIMINARI-----//
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security_user.contains(Owner))
			throw new UserNotFoundException();

		if (!security_pass.get(security_user.indexOf(Owner)).equals(passw))
			throw new WrongPasswordException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Aggiungo ai files di Owner l'oggetto MyFile<E> (di tipo astratto File<E>) che incapsula, tra le altre cose, l'argomento E file passato.
		data.get(security_user.indexOf(Owner)).add(new MyFile<E>(Owner, file));
		
		// Ritorno true ad aggiunta effettuata, non ritorno mai false perchè sempre preceduto da un'eccezione
		return true;
	}
	/*
	 * Questo metodo preserva l'IR perchè non rischia di portare nessuna struttura dati e nessun campo a null, si limita ad aggiungere un MyFile<E> 
	 * nell'ArrayList<File<E>> proprio di Owner.
	 */

	// Ottiene una copia del file nella collezione se vengono rispettati i controlli di identità
	public E get(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		
		//-----CONTROLLI PRELIMINARI-----//

		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security_user.contains(Owner))
			throw new UserNotFoundException();

		if (!security_pass.get(security_user.indexOf(Owner)).equals(passw))
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
					System.out.println("[DOWNLOAD] Il file: <" + file + "> che " + Owner + " sta per scaricare, è stato condiviso da " + check.getOwner() + " in sola lettura!");
				else
					System.out.println("[DOWNLOAD] Il file: <" + file + "> che " + Owner + " sta per scaricare, è stato condiviso da " + check.getOwner() + " in lettura e scrittura!");
			}
			else 					
				// Owner è il proprietario legittimo e quindi può accedere sia in scrittura che lettura al file che sta scaricando
				System.out.println("[DOWNLOAD] Il file: <" + file + "> che stai per scaricare, è di tua proprietà, con accesso in scrittura e lettura");
			
			// Ritorno il file E
			return check.getData();
		}
	}
	/*
	 * Questo metodo preserva l'IR perchè non va a modificare nulla, si limita a ritornare, se trovato, una copia del file cercato dall'utente Owner 
	 */

	// Rimuove il file dalla collezione se vengono rispettati i controlli di identità
	public E remove(String Owner, String passw, E file)	throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		
		//-----CONTROLLI PRELIMINARI-----//

		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security_user.contains(Owner))
			throw new UserNotFoundException();

		if (!security_pass.get(security_user.indexOf(Owner)).equals(passw))
			throw new WrongPasswordException();
		
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Il metodo searchFor è un metodo privato, proprio di questa classe. (scendere al metodo in questione per i commenti sul suo funzionamento)
		File<E> check = this.searchFor(Owner, file, null);
		
		if (check==null)
			throw new NoDataException();
		else{
			
			if (!check.getOwner().equals(Owner)) // Sto cercando di eliminare un file che mi è stato condiviso e che non è quindi di mia proprietà
				data.get(security_user.indexOf(Owner)).remove(check); // Lo rimuovo semplicemente
			else {
				// Se sto invece cercando di rimuovere un file di mia proprietà, lo rimuovo anche a coloro a cui l'avevo condiviso
				File<E> shareremove;
				String shareuser;
				
				Iterator<String> it = check.getIteratorRead(); 									// Ottengo un iteratore sugli utenti che hanno accesso in sola lettura a check
				while (it.hasNext()) {
					shareuser = it.next();
					shareremove = this.searchFor(shareuser, file, Owner);						// Cerco tra i files di shareuser (utente a cui è stato condiviso check in sola lettura) un file 
																	 							// uguale al file che sto rimuovendo e di proprietà di Owner.
					if(shareremove!=null)		
						data.get(security_user.indexOf(shareuser)).remove(shareremove);			// Se lo trovo, lo rimuovo
				}
				
				it = check.getIteratorWrite();													// Ottengo un iteratore sugli utenti che hanno accesso in lettura/scrittura a check
				while (it.hasNext()) {
					shareuser = it.next();
					shareremove = this.searchFor(shareuser, file, Owner); 						// Cerco tra i files di shareuser (utente a cui è stato condiviso check in lettura/scrittura) un file 
					 												 							// uguale al file che sto rimuovendo e di proprietà di Owner.
					if(shareremove!=null)
						data.get(security_user.indexOf(shareuser)).remove(shareremove);			// Se lo trovo, lo rimuovo
				}
			}
		}
		return file;
	}
	/*
	 * Questo metodo preserva l'IR perchè si limita a rimuovere dei file da delle collezioni di utenti senza inficiare alcuna condizione posta dall'IR. (non porta nessuna struttura dati
	 * a null e non spezza la condizione di non duplicità)
	 */

	// Crea una copia del file nella collezione se vengono rispettati i controlli di identità
	public void copy(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {

		//-----CONTROLLI PRELIMINARI-----//

		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security_user.contains(Owner))
			throw new UserNotFoundException();

		if (!security_pass.get(security_user.indexOf(Owner)).equals(passw))
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
	 * Questo metodo preserva l'IR perchè si limita a copiare un file di data.get(security_user.indexOf(Owner), in data.get(security_user.indexOf(Owner) stesso, 
	 * senza inficiare alcuna condizione posta dall'IR (non porta nulla a null e non spezza la condizione di non duplicità).
	 * La condizione di non duplicità si applica agli username ma non ai dati contenuti in ogni ArrayList<File<E>> che invece ammettono duplicati.
	 */

	// Condivide in lettura il file nella collezione con un altro utente se vengono rispettati i controlli di identità
	public void shareR(String Owner, String passw, String Other, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException, IllegalSharingException {
		
		//-----CONTROLLI PRELIMINARI-----//

		if (Owner==null || passw==null || Other==null || file==null)
			throw new NullPointerException();
		
		if (!security_user.contains(Owner))
			throw new UserNotFoundException();
		
		if (!security_user.contains(Other))
			throw new UserNotFoundException();
		
		if (!security_pass.get(security_user.indexOf(Owner)).equals(passw))
			throw new WrongPasswordException();
		
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		// Se Owner vuole condividere un file a se stesso
		if (Other.equals(Owner))
			throw new IllegalSharingException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Il metodo searchFor è un metodo privato, proprio di questa classe. (scendere al metodo in questione per i commenti sul suo funzionamento)
		File<E> check = this.searchFor(Owner, file, null);
		
		// Se Owner vuole condividere un file di cui non è proprietario lancio un'eccezione
		if (check!=null && !check.getOwner().equals(Owner))
			throw new IllegalSharingException();
		
		if (check==null)
			throw new NoDataException();
		else {
			
			// Il metodo setShareR è un metodo pubblico fornito da File<E>, in poche parole fa in modo che il file check si "ricordi" di essere stato condiviso in lettura
			// all'utente Other (andare al metodo in questione per i commenti specifici sul suo funzionamento)
			check.setShareR(Other);
			
			// Aggiungo all'ArrayList<File<E>> proprio di Other, il file check, SOLO SE non era già presente.
			// Nel caso fosse già presente mi basta aggiornare i permessi, cosa che ho fatto col comando precedente
			if(this.searchFor(Other,file, Owner) == null)
				data.get(security_user.indexOf(Other)).add(check);
		}
	}
	/*
	 * Questo metodo preserva l'IR perchè aggiunge semplicemente un file a data.get(security_user.indexOf(Other)) senza compromettere nessuna condizione dell'IR.
	 */

	// Condivide in lettura e scrittura il file nella collezione con un altro utente se vengono rispettati i controlli di identità
	public void shareW(String Owner, String passw, String Other, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException, IllegalSharingException {
		
		//-----CONTROLLI PRELIMINARI-----//

		if (Owner==null || passw==null || Other==null || file==null)
			throw new NullPointerException();
		
		if (!security_user.contains(Owner))
			throw new UserNotFoundException();
		
		if (!security_user.contains(Other))
			throw new UserNotFoundException();
		
		if (!security_pass.get(security_user.indexOf(Owner)).equals(passw))
			throw new WrongPasswordException();
		
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		// Se Owner vuole condividere un file a se stesso
		if (Other.equals(Owner))
			throw new IllegalSharingException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Il metodo searchFor è un metodo privato, proprio di questa classe. (scendere al metodo in questione per i commenti sul suo funzionamento)
		File<E> check = this.searchFor(Owner, file, null);
		
		// Se Owner vuole condividere un file di cui non è proprietario lancio un'eccezione
		if (check!=null && !check.getOwner().equals(Owner))
			throw new IllegalSharingException();
		
		if (check==null)
			throw new NoDataException();
		else {
			
			// Il metodo setShareW è un metodo pubblico fornito da File<E>, in poche parole fa in modo che il file check si "ricordi" di essere stato condiviso in lettura e scrittura
			// all'utente Other (andare al metodo in questione per i commenti specifici sul suo funzionamento)
			check.setShareW(Other);
			
			// Aggiungo all'ArrayList<File<E>> proprio di Other, il file check, SOLO SE non era già presente.
			// Nel caso fosse già presente mi basta aggiornare i permessi, cosa che ho fatto col comando precedente
			if(this.searchFor(Other,file, Owner) == null)
				data.get(security_user.indexOf(Other)).add(check);
		}
		
	}
	/*
	 * Questo metodo preserva l'IR perchè aggiunge semplicemente un file a data.get(security_user.indexOf(Other)) senza compromettere nessuna condizione dell'IR.
	 */

	// Restituisce un iteratore (senza remove) che genera tutti i file dell’utente in ordine arbitrario se vengono rispettati i controlli di identità
	public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
	
		//-----CONTROLLI PRELIMINARI-----//

		if (Owner==null || passw==null)
			throw new NullPointerException();
		
		if (!security_user.contains(Owner))
			throw new UserNotFoundException();
		
		if (!security_pass.get(security_user.indexOf(Owner)).equals(passw))
			throw new WrongPasswordException();
		
		if (this.getSize(Owner, passw)==0)
			throw new NoDataException();
		
		//-----FINE CONTROLLI PRELIMINARI-----//
		
		// Creo un ArrayList<E> da popolare
		ArrayList<E> temp = new ArrayList<E>();
		
	    // Popolo l'ArrayList<E> precedentemente istanziato, dei file E (incapsulati nei vari File<E>) relativi all'utente Owner
		for (int i=0; i<data.get(security_user.indexOf(Owner)).size(); i++){
			  File<E> p = data.get(security_user.indexOf(Owner)).get(i);
			  temp.add(p.getData());
		}
		
		// Costruisco l'iteratore sull'ArrayList<E> precedente e lo ritorno
		Iterator<E> it = temp.iterator();
		return it;
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */
	
	// Il compito di questo metodo è rintracciare file, tra i files di Owner. (verrà ritornato il File<E> che lo incapsula e non file direttamente)
	// Con realOwner==null verrà semplicemente rintracciato il primo file uguale a quello ricercato.
	// Con realOwner!=null verrà rintracciato il primo file uguale a quello ricercato, E INOLTRE, di proprietà di realOwner, quindi un file CONDIVISO SPECIFICO.
	// Se il file ricercato non dovesse esistere, verrà ritornato null.
	private File<E> searchFor(String Owner, E file, String realOwner) {
		
		// Se Owner non ha alcun file salvato è inutile che continui la ricerca e ritorno direttamente null
		if(data.get(security_user.indexOf(Owner)).size()==0)
			return null;

		// Preparo un arraylist temporaneo dove ciclare per cercare il file 
		ArrayList<File<E>> files_temp = data.get(security_user.indexOf(Owner));	
		
		// Estraggo il primo file contenuto
		File<E> temp =  files_temp.get(0);		
		int i=1;
		
		if(realOwner == null) {
			// Ciclo fino a che non trovo il primo file uguale a quello ricercato
			while(!temp.getData().equals(file) && i<files_temp.size()){	
				temp = files_temp.get(i);
				i++;
			}
			
			if(temp.getData().equals(file))								
				// Ritorno il File<E> che incapsula il file ricercato 
				return temp;
			else
				return null;
		}
		else {
			// Ciclo fino a che non trovo il primo file uguale a quello ricercato, E INOLTRE, di proprietà di realOwner
			while( (!temp.getData().equals(file) || !temp.getOwner().equals(realOwner)) && i<files_temp.size()){	
				temp = files_temp.get(i);
				i++;
			}
			
			if(temp.getData().equals(file) && temp.getOwner().equals(realOwner))
				// Ritorno il File<E> che incapsula il file ricercato, di proprietà di realOwner
				return temp;
			else
				return null;
		}
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */
}