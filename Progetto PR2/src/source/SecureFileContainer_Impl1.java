package source;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

public class SecureFileContainer_Impl1<E> implements SecureFileContainer<E> {
	
	private HashMap<String, String> security; // struttura dati per il controllo degli accessi ai dati, attraverso l'associazione utente password
	private HashMap<String, ArrayList<File<E>>> data; // struttura dati per il salvataggio dei file relativi ad un utente
	
	public SecureFileContainer_Impl1() {
		security = new HashMap<String, String>(0);
		data = new HashMap<String, ArrayList<File<E>>>(0);
	}


	public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyRegisteredException, WeakPasswordException, IllegalUsernameException {
		
		if (Id==null || passw==null)
			throw new NullPointerException();
		
		if (security.containsKey(Id))
			throw new UserAlreadyRegisteredException(Id + " è già registrato!");
		
		if (passw.length() < 5)
			throw new WeakPasswordException("La tua password è troppo corta!");
		
		if (Id.length() < 2)
			throw new IllegalUsernameException("Il tuo username è troppo corto!");
		
	    security.put(Id, passw);
        data.put(Id, new ArrayList<File<E>>(0));	
	}


	public int getSize(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		if (Owner==null || passw==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		return data.get(Owner).size();
		
	}


	public boolean put(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		if (Owner==null || passw==null || file==null)
			throw new NullPointerException();
		
		if (!security.containsKey(Owner))
			throw new UserNotFoundException("L'utente non è registrato al file storage!");
		
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException("Hai inserito una password sbagliata!");
		
		data.get(Owner).add(new MyFile<E>(Owner, file));

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
		
		E temp =  data.get(Owner).get(0).getData();
		int i=1;
		
		while(!temp.equals(file) && i<data.get(Owner).size()){
			temp = data.get(Owner).get(i).getData();
			i++;
		}
		
		if (temp!=file)
			throw new NoDataException("Il file da te richiesto non esiste nel file storage!");
		else {
			File<E> check = data.get(Owner).get(i-1);
			if(!check.getOwner().equals(Owner)) {
				if(check.isSharedR(Owner))
					System.out.println("Il file: <" + file + "> che stai per scaricare, ti è stato condiviso da " + check.getOwner() + " in sola lettura!");
				else
					System.out.println("Il file: <" + file + "> che stai per scaricare ti è stato condiviso da " + check.getOwner() + " in lettura/scrittura!");
			}
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
		
		E temp =  data.get(Owner).get(0).getData();
		int i=1;
		
		while(!temp.equals(file) && i<data.get(Owner).size()){
			temp = data.get(Owner).get(i).getData();
			i++;
		}
		
		
		if (temp!=file)
			throw new NoDataException("Il file che vuoi rimuovere non esiste nel file storage!");
		else
			data.get(Owner).remove(data.get(Owner).get(i-1));
		
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
		
		E temp =  data.get(Owner).get(0).getData();
		int i=1;
		
		while(!temp.equals(file) && i<data.get(Owner).size()){
			temp = data.get(Owner).get(i).getData();
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
		
		E temp =  data.get(Owner).get(0).getData();
		int i=1;
		
		while(!temp.equals(file) && i<data.get(Owner).size()){
			temp = data.get(Owner).get(i).getData();
			i++;
		}
		
		
		if (temp!=file)
			throw new NoDataException("Il file che vuoi condividere in lettura, non esiste nel file storage!");
		else {
			data.get(Owner).get(i-1).setShareR(Other);
			data.get(Other).add(data.get(Owner).get(i-1));
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
