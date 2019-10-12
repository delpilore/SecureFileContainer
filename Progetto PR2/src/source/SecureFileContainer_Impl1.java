package source;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

public class SecureFileContainer_Impl1<E> implements SecureFileContainer<E> {
	
    // AF(c) = { < Id, c.security.get(Id), <c.data.get(Id)> > | c.security.containsKey(Id) }
	
    // IR = (c.security != null) && (c.data != null)
    //		&& (c.data.containsKey(Id) => c.security.containsKey(Id))
	//		&& (c.security.containsKey(Id) => c.data.get(Id) != null
	//		&& (a = c.security.keySet(), for all i,j. 0 <= i,j < a.size(), a(i) != a(j))
	//		&& (b = c.data.keySet(), for all i,j. 0 <= i,j < b.size(), b(i) != b(j))
	
	private HashMap<String, String> security; 				// Struttura dati per il controllo degli accessi ai dati, attraverso l'associazione utente password
	private HashMap<String, ArrayList<File<E>>> data; 		// Struttura dati per il salvataggio dei file relativi ad un utente
	
	// Metodo costruttore che inizializza le due strutture dati 
	public SecureFileContainer_Impl1() {
		security = new HashMap<String, String>(0);
		data = new HashMap<String, ArrayList<File<E>>>(0);	// L'array che conterrà i file, conterrà oggetti di tipo File<E> (interfaccia e implementazione nei file specifici)
	}

	// Metodo per la creazione di un utente della collezione
	public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyRegisteredException, WeakPasswordException, IllegalUsernameException {
		
		if (Id==null || passw==null)
			throw new NullPointerException();
		
		if (security.containsKey(Id))
			throw new UserAlreadyRegisteredException(Id + " è già registrato!");
		
		if (passw.length() < 5)
			throw new WeakPasswordException("La tua password è troppo corta!");
		
		if (Id.length() < 2)
			throw new IllegalUsernameException("Il tuo username è troppo corto!");
		
	    security.put(Id, passw); 							// Aggiungo alla struttura dati per il controllo degli accessi futuri, l'associazione tra l'username Id e la password passw
        data.put(Id, new ArrayList<File<E>>(0)); 			// Aggiungo alla struttura dati per il salvataggio dei file, l'associazione tra l'username Id e un oggetto ArrayList inizializzato a 0 e istanziato per contenere File<E>
	}


	public int getSize(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		if (Owner==null || passw==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		return data.get(Owner).size();						// Restituisco la size() dell'ArrayList di File<E> relativo al utente Owner
		
	}


	public boolean put(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		data.get(Owner).add(new MyFile<E>(Owner, file));	// Aggiungo all'ArrayList di File<E>, proprio di Owner, il MyFile<E> che incapsula file passato come argomento

		return true;
		
	}

	public E get(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {

		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		if (data.get(Owner).size()==0)
			throw new NoDataException("Il tuo file storage è vuoto!");
		
		ArrayList<File<E>> files_temp = data.get(Owner);	// Prendo l'ArrayList di File<E> relativo a Owner e lo comincio a scorrere
		
		E temp =  files_temp.get(0).getData();				// getData() serve per estrarre dal File<E> il dato E
		int i=1;
		
		while(!temp.equals(file) && i<files_temp.size()){	// Ciclo finchè non trovo il file cercato
			temp = files_temp.get(i).getData();
			i++;
		}
		
		if (temp!=file)
			throw new NoDataException("Il file da te richiesto non esiste nel file storage!");
		else {
			File<E> check = files_temp.get(i-1);
			if(!check.getOwner().equals(Owner)) {			// Controllo se il file che l'utente sta cercando di scaricare è condiviso da un altro utente
				if(check.isSharedR(Owner))
					System.out.println("[Download] Il file: <" + file + "> che " + Owner + " sta per scaricare, è stato condiviso da " + check.getOwner() + " in sola lettura!");
				else
					System.out.println("[Download] Il file: <" + file + "> che " + Owner + " sta per scaricare, è stato condiviso da " + check.getOwner() + " in lettura e scrittura!");
			}
			// Se il file è di proprietà di Owner passo direttamente alla return
			return temp;
		}
	}


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
