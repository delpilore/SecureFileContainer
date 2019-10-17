package source;

import java.util.Iterator;

// AUTHOR: Lorenzo Del Prete, Corso B, 531417

public interface SecureFileContainer<E> {
	
	/*
	 * OVERVIEW: Gli oggetti di tipo SecureFileContainer<E> sono collezioni modificabili di triple <username, password, <files E>>. 
	 * 			 Ogni utente, identificato dalla coppia <username, password>, ha un accesso protetto ai suoi <files E>: può inserirne di nuovi, rimuoverne di vecchi e può
	 * 			 decidere di condividerli con altri utenti in sola lettura o in lettura/scrittura.
	 * 
	 * TYPICAL ELEMENT: <user_i, psw_i, <file_0, file_1, ..., file_n>>
	 * 				    user_i: username
	 *                  psw_i: password
	 *                  <file_0, ..., file_n>: insieme di files E salvati nella collezione, associati all'utente identificato da <user_i, passw_i>
	 */
	
	// Crea l’identità di un nuovo utente della collezione
	public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyRegisteredException, WeakPasswordException, IllegalUsernameException;
	/*
	 * REQUIRES: Id!=null && passw!=null
	 * THROWS: Se Id==null || passw==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   Se Id esiste già nella collezione -> UserAlreadyRegisteredException (eccezione non disponibile in Java, checked)
	 * 		   Se passw.length < 5 -> WeakPasswordException (eccezione non disponibile in Java, checked)
	 * 		   Se Id.length < 2 -> IllegalUsernameException (eccezione non disponibile in Java, checked)
	 * MODIFIES: this
	 * EFFECTS: Viene creato ed inserito nella collezione, l'elemento <Id, passw, <>>
	 */
	
	// Restituisce il numero dei file di un utente presenti nella collezione
	public int getSize(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException;
	/*
	 * REQUIRES: Owner!=null && passw!=null
	 * THROWS: Se Owner==null || passw==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   Se Owner non esiste nella collezione -> UserNotFoundException (eccezione non disponibile in Java, checked)
	 * 		   Se Owner esiste nella collezione ma passw non corrisponde -> WrongPasswordException (eccezione non disponibile in Java, checked)
	 * RETURN: Numero dei <files E>, relativi alla tripla <Owner, passw, <files E>>
	 */
	
	// Inserisce il file nella collezione se vengono rispettati i controlli di identità
	public boolean put(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException;
	/*
	 * REQUIRES: Owner!=null && passw!=null && file!=null
	 * THROWS: Se Owner==null || passw==null || file==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   Se Owner non esiste nella collezione -> UserNotFoundException (eccezione non disponibile in Java, checked)
	 * 		   Se Owner esiste nella collezione ma passw non corrisponde -> WrongPasswordException (eccezione non disponibile in Java, checked)
	 * MODIFIES: this
	 * EFFECTS: Viene inserito il file E, nel campo <files E> della tripla <Owner, passw, <files E>>
	 * RETURN: true quando il file E viene inserito, false altrimenti
	 */
	
	// Ottiene una copia del file nella collezione se vengono rispettati i controlli di identità
	public E get(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException;
	/*
	 * REQUIRES: Owner!=null && passw!=null && file!=null
	 * THROWS: Se Owner==null || passw==null || file==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   Se Owner non esiste nella collezione -> UserNotFoundException (eccezione non disponibile in Java, checked)
	 * 		   Se Owner esiste nella collezione ma passw non corrisponde -> WrongPasswordException (eccezione non disponibile in Java, checked)
	 * 		   Se la coppia <Owner,passw> esiste nella collezione, ma non esiste il file E da ottenere -> NoDataException (eccezione non disponibile in Java, checked)
	 * RETURN: Una copia del file E, rintracciato nel campo <files E> della tripla <Owner, passw, <files E>>
	 */
	
	// Rimuove il file dalla collezione se vengono rispettati i controlli di identità
	public E remove(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException;
	/*
	 * REQUIRES: Owner!=null && passw!=null && file!=null
	 * THROWS: Se Owner==null || passw==null || file==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   Se Owner non esiste nella collezione -> UserNotFoundException (eccezione non disponibile in Java, checked)
	 * 		   Se Owner esiste nella collezione ma passw non corrisponde -> WrongPasswordException (eccezione non disponibile in Java, checked)
	 * 		   Se la coppia <Owner,passw> esiste nella collezione, ma non esiste il file E da rimuovere -> NoDataException (eccezione non disponibile in Java, checked)
	 * MODIFIES: this
	 * EFFECTS: Viene rimosso il file E, dal campo <files E> della tripla <Owner, passw, <files E>>
	 * RETURN: Il file che è stato rimosso
	 */
	
	// Crea una copia del file nella collezione se vengono rispettati i controlli di identità
	public void copy(String Owner, String passw, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException;
	/*
	 * REQUIRES: Owner!=null && passw!=null && file!=null
	 * THROWS: Se Owner==null || passw==null || file==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   Se Owner non esiste nella collezione -> UserNotFoundException (eccezione non disponibile in Java, checked)
	 * 		   Se Owner esiste nella collezione ma passw non corrisponde -> WrongPasswordException (eccezione non disponibile in Java, checked)
	 * 		   Se la coppia <Owner,passw> esiste nella collezione, ma non esiste il file E da copiare -> NoDataException (eccezione non disponibile in Java, checked)
	 * MODIFIES: this
	 * EFFECTS: Viene inserito un duplicato del file E, nel campo <files E> della tripla <Owner, passw, <files E>>
	 */
	
	// Condivide in lettura il file nella collezione con un altro utente se vengono rispettati i controlli di identità
	public void shareR(String Owner, String passw, String Other, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException;
	/*
	 * REQUIRES: Owner!=null && passw!=null && Other!=null && file!=null
	 * THROWS: Se Owner==null || passw==null || Other==null || file==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   Se Owner || Other non esistono nella collezione -> UserNotFoundException (eccezione non disponibile in Java, checked)
	 * 		   Se Owner esiste nella collezione ma passw non corrisponde -> WrongPasswordException (eccezione non disponibile in Java, checked)
	 * 		   Se la coppia <Owner, passw> esiste nella collezione, ma non esiste il file E da condividere in lettura -> NoDataException (eccezione non disponibile in Java, checked)
	 * MODIFIES: this
	 * EFFECTS: Il campo <files E> della tripla <Other, passw, <files E>> sarà arricchito dal file E condiviso da Owner, disponibile in sola lettura.
	 */
	
	// Condivide in lettura e scrittura il file nella collezione con un altro utente se vengono rispettati i controlli di identità
	public void shareW(String Owner, String passw, String Other, E file) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException;
	/*
	 * REQUIRES: Owner!=null && passw!=null && Other!=null && file!=null
	 * THROWS: Se Owner==null || passw==null || Other==null || file==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   Se Owner || Other non esistono nella collezione -> UserNotFoundException (eccezione non disponibile in Java, checked)
	 * 		   Se Owner esiste nella collezione ma passw non corrisponde -> WrongPasswordException (eccezione non disponibile in Java, checked)
	 * 		   Se la coppia <Owner, passw> esiste nella collezione, ma non esiste il file E da condividere in lettura e scrittura -> NoDataException (eccezione non disponibile in Java, checked)
	 * MODIFIES: this
	 * EFFECTS: Il campo <files E> della tripla <Other, passw, <files E>> sarà arricchito dal file E condiviso da Owner, disponibile in lettura e scrittura.
	 */
	
	// Restituisce un iteratore (senza remove) che genera tutti i file dell’utente in ordine arbitrario se vengono rispettati i controlli di identità
	public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException;
	/*
	 * REQUIRES: Owner!=null && passw!=null
	 * THROWS: Se Owner==null || passw==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   Se Owner non esiste nella collezione -> UserNotFoundException (eccezione non disponibile in Java, checked)
	 * 		   Se Owner esiste nella collezione ma passw non corrisponde -> WrongPasswordException (eccezione non disponibile in Java, checked)
	 * 		   Se la coppia <Owner, passw> esiste nella collezione, ma l'insieme <files E> relativo è vuoto -> NoDataException  (eccezione non disponibile in Java, checked)
	 * RETURN: Iteratore sui <files E> relativi alla tripla <Owner, passw, <files E>>
	 */
	
	
	/*
	 * MY CHECKED EXCEPTIONS
	 */
	
	@SuppressWarnings("serial")
	// Eccezione lanciata quando si tenta di iscrivere un Utente già iscritto (eccezione checked, non presente in java)
	class UserAlreadyRegisteredException extends Exception {

		public UserAlreadyRegisteredException() {
            super();
        }
        
        public UserAlreadyRegisteredException(String s) {
            super(s);
        }
    }
	
	@SuppressWarnings("serial")
	// Eccezione lanciata quando si tenta di iscrivere un Utente con una password più corta di 5 caratteri (eccezione checked, non presente in java)
	class WeakPasswordException extends Exception {

		public WeakPasswordException() {
            super();
        }
        
        public WeakPasswordException(String s) {
            super(s);
        }
    }
	
	@SuppressWarnings("serial")
	// Eccezione lanciata quando viene richiesta un'operazione su un utente che non è iscritto alla collezione (eccezione checked, non presente in java)
	class UserNotFoundException extends Exception {

		public UserNotFoundException() {
            super();
        }
        
        public UserNotFoundException(String s) {
            super(s);
        }
    }
	
	@SuppressWarnings("serial")
	// Eccezione lanciata quando, durante il controllo accesso, l'utente iscritto alla collezione inserisce la password sbagliata (eccezione checked, non presente in java)
	class WrongPasswordException extends Exception {

		public WrongPasswordException() {
            super();
        }
        
        public WrongPasswordException(String s) {
            super(s);
        }
    }
	
	@SuppressWarnings("serial")
	// Eccezione lanciata quando un utente tenta di fare un'operazione su un dato che non esiste nella sua collezione di files E (eccezione checked, non presente in java)
	class NoDataException extends Exception {

		public NoDataException() {
            super();
        }
        
        public NoDataException(String s) {
            super(s);
        }
    }
	
	@SuppressWarnings("serial")
	// Eccezione lanciata quando si tenta di iscrivere un Utente con un username più corto di 2 caratteri (eccezione checked, non presente in java)
	class IllegalUsernameException extends Exception {

		public IllegalUsernameException() {
            super();
        }
        
        public IllegalUsernameException(String s) {
            super(s);
        }
    }
}
