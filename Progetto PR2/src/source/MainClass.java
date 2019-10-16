package source;

import java.util.Iterator;

import source.SecureFileContainer.IllegalUsernameException;
import source.SecureFileContainer.NoDataException;
import source.SecureFileContainer.UserAlreadyRegisteredException;
import source.SecureFileContainer.UserNotFoundException;
import source.SecureFileContainer.WeakPasswordException;
import source.SecureFileContainer.WrongPasswordException;

public class MainClass {

	public static void main(String[] args){
		
		SecureFileContainer<String> dropbox = new SecureFileContainer_Impl1<String>();
		int a;
		String receive;
	
		try { //test createUser, getSize, put, get, remove, copy, getIterator (senza arrivo di eccezioni)
			System.out.println("---AVVIO FILE STORAGE DROPBOX---");
			
			dropbox.createUser("pippo", "12345");
			System.out.println("[USER REGISTERED] L'utente: <pippo> si è registrato!");
			dropbox.createUser("giovanni", "12345");
			System.out.println("[USER REGISTERED] L'utente: <giovanni> si è registrato!");
			
			dropbox.put("pippo", "12345", "file1");
			System.out.println("[File Uploaded] L'utente: <pippo> ha caricato il file: <file1>");
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			dropbox.put("pippo", "12345", "file2");
			System.out.println("[File Uploaded] L'utente: <pippo> ha caricato il file: <file2>");
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			dropbox.put("pippo", "12345", "file3");
			System.out.println("[File Uploaded] L'utente: <pippo> ha caricato il file: <file3>");
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			dropbox.put("pippo", "12345", "file4");
			System.out.println("[File Uploaded] L'utente: <pippo> ha caricato il file: <file4>");
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			receive = dropbox.get("pippo", "12345", "file1");
			System.out.println("[Download] L'utente <pippo> ha scaricato una copia di " + receive);
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			receive = dropbox.remove("pippo", "12345", "file1");
			System.out.println("[Remove] L'utente <pippo> ha rimosso " + receive);
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			receive = dropbox.remove("pippo", "12345", "file4");
			System.out.println("[Remove] L'utente <pippo> ha rimosso " + receive);
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			dropbox.copy("pippo", "12345", "file2");
			System.out.println("[Copy] L'utente <pippo> ha copiato il file: <file2>");
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			dropbox.copy("pippo", "12345", "file2");
			System.out.println("[Copy] L'utente <pippo> ha copiato il file: <file2>");
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			// a questo punto dovrei avere 3 file2 e 1 file3
			
			Iterator<String> it = dropbox.getIterator("pippo","12345");
			while (it.hasNext()) {
			  String p = it.next();
			  System.out.println(p);
			}
			
			System.out.println("*****PROVA SHARING*****");
			
			dropbox.put("giovanni", "12345", "file5");
			System.out.println("[File Uploaded] L'utente: <giovanni> ha caricato il file: <file5>");
			a = dropbox.getSize("giovanni", "12345");
			System.out.println("[Size File Storage] L'utente <giovanni> ha " + a + " file salvati");
			
			dropbox.put("giovanni", "12345", "file1");
			System.out.println("[File Uploaded] L'utente: <giovanni> ha caricato il file: <file1>");
			a = dropbox.getSize("giovanni", "12345");
			System.out.println("[Size File Storage] L'utente <giovanni> ha " + a + " file salvati");
			
			dropbox.shareW("pippo", "12345", "giovanni", "file2");
			System.out.println("[SHARING] L'utente: <pippo> ha condiviso in scrittura il file <file2> all'utente <giovanni>");
			a = dropbox.getSize("giovanni", "12345");
			System.out.println("[Size File Storage] L'utente <giovanni> ha " + a + " file salvati");
			
			dropbox.shareR("pippo", "12345", "giovanni", "file3");
			System.out.println("[SHARING] L'utente: <pippo> ha condiviso in lettura il file <file3> all'utente <giovanni>");
			a = dropbox.getSize("giovanni", "12345");
			System.out.println("[Size File Storage] L'utente <giovanni> ha " + a + " file salvati");
			
			dropbox.shareW("giovanni", "12345", "pippo", "file1");
			System.out.println("[SHARING] L'utente: <giovanni> ha condiviso in scrittura il file <file1> all'utente <pippo>");
			a = dropbox.getSize("pippo", "12345");
			System.out.println("[Size File Storage] L'utente <pippo> ha " + a + " file salvati");
			
			System.out.println("*****PIPPO*****"); //pippo dovrebbe avere 3 file 2, 1 file 3, 1 file 1, in totale 5 file
			
			it = dropbox.getIterator("pippo","12345");
			while (it.hasNext()) {
			  String p = it.next();
			  System.out.println(p);
			}
			 
			System.out.println("*****GIOVANNI*****"); //giovanni dovrebbe avere 1 file 5, 1 file 1, 1 file 2 e 1 file 3
			
			it = dropbox.getIterator("giovanni","12345");
			while (it.hasNext()) {
			  String p = it.next();
			  System.out.println(p);
			}
			
			receive = dropbox.get("pippo", "12345", "file1");
			System.out.println("[Download] L'utente <pippo> ha scaricato una copia di " + receive);
			
			receive = dropbox.get("pippo", "12345", "file2");
			System.out.println("[Download] L'utente <pippo> ha scaricato una copia di " + receive);
			
			receive = dropbox.get("pippo", "12345", "file3");
			System.out.println("[Download] L'utente <pippo> ha scaricato una copia di " + receive);
			
			receive = dropbox.get("giovanni", "12345", "file1");
			System.out.println("[Download] L'utente <giovanni> ha scaricato una copia di " + receive);
			
			receive = dropbox.get("giovanni", "12345", "file2");
			System.out.println("[Download] L'utente <giovanni> ha scaricato una copia di " + receive); // condiviso in lettura scrittura da pippo
			
			receive = dropbox.get("giovanni", "12345", "file3");
			System.out.println("[Download] L'utente <giovanni> ha scaricato una copia di " + receive); // condiviso in sola lettura da pippo
			
			receive = dropbox.get("giovanni", "12345", "file5");
			System.out.println("[Download] L'utente <giovanni> ha scaricato una copia di " + receive);
			
			System.out.println("*****PIPPO*****"); //pippo dovrebbe avere 3 file 2, 1 file 3, 1 file 1, in totale 5 file
			
			it = dropbox.getIterator("pippo","12345");
			while (it.hasNext()) {
			  String p = it.next();
			  System.out.println(p);
			}
			 
			System.out.println("*****GIOVANNI*****"); //giovanni dovrebbe avere 1 file 5, 1 file 1, 1 file 2 e 1 file 3
			
			it = dropbox.getIterator("giovanni","12345");
			while (it.hasNext()) {
			  String p = it.next();
			  System.out.println(p);
			}
			
			receive = dropbox.remove("giovanni", "12345", "file2");
			receive = dropbox.remove("giovanni", "12345", "file3");
			
			System.out.println("*****PIPPO*****"); //pippo dovrebbe avere 3 file 2, 1 file 3, 1 file 1, in totale 5 file
			
			it = dropbox.getIterator("pippo","12345");
			while (it.hasNext()) {
			  String p = it.next();
			  System.out.println(p);
			}
			 
			System.out.println("*****GIOVANNI*****"); //giovanni dovrebbe avere 1 file 5, 1 file 1, 1 file 2 e 1 file 3
			
			it = dropbox.getIterator("giovanni","12345");
			while (it.hasNext()) {
			  String p = it.next();
			  System.out.println(p);
			}
			
			

		} catch (NullPointerException | UserAlreadyRegisteredException | WeakPasswordException | IllegalUsernameException | UserNotFoundException | WrongPasswordException | NoDataException e) {
			e.printStackTrace();
		}
		
		
		
		
	/*	
		System.out.println("-------------------------");
		SecureFileContainer<Number> dropbox2 = new SecureFileContainer_Impl1<Number>();
		
		try {
			dropbox2.createUser("marco","12345");
			
			int b = dropbox2.getSize("marco", "12345");
			System.out.println("[Size File Storage] marco ha " + b + " file salvati");
			
			dropbox2.put("marco", "12345", 6);
			b = dropbox2.getSize("marco", "12345");
			System.out.println("[Size File Storage] marco ha " + b + " file salvati");
			
			dropbox2.put("marco", "12345", 6);
			b = dropbox2.getSize("marco", "12345");
			System.out.println("[Size File Storage] marco ha " + b + " file salvati");
			
			Number c = dropbox2.get("marco","12345", 100);
			System.out.println("[Download] marco ha scaricato una copia di " + c);
			b = dropbox2.getSize("marco", "12345");
			System.out.println("[Size File Storage] marco ha " + b + " file salvati");
			
		} catch (NullPointerException | UserAlreadyRegisteredException | WeakPasswordException
				| IllegalUsernameException | UserNotFoundException | WrongPasswordException | NoDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	*/
		

	}

}
