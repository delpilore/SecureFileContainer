package source;

import source.SecureFileContainer.IllegalUsernameException;
import source.SecureFileContainer.NoDataException;
import source.SecureFileContainer.UserAlreadyRegisteredException;
import source.SecureFileContainer.UserNotFoundException;
import source.SecureFileContainer.WeakPasswordException;
import source.SecureFileContainer.WrongPasswordException;

public class MainClass {

	public static void main(String[] args){
		
		SecureFileContainer<String> dropbox = new SecureFileContainer_Impl1<String>();
	
		try {
			dropbox.createUser("cicchio", "12345");
			dropbox.createUser("giovanni", "12345");
			
			int a = dropbox.getSize("cicchio", "12345");
			System.out.println("[Size File Storage] cicchio ha " + a + " file salvati");
			
			dropbox.put("cicchio", "12345", "file1");
			a = dropbox.getSize("cicchio", "12345");
			System.out.println("[Size File Storage] cicchio ha " + a + " file salvati");
			
			dropbox.put("cicchio", "12345", "file2");
			a = dropbox.getSize("cicchio", "12345");
			System.out.println("[Size File Storage] cicchio ha " + a + " file salvati");
			
			String receive = dropbox.get("cicchio", "12345", "file1");
			System.out.println("[Download] cicchio ha scaricato una copia di " + receive);
			a = dropbox.getSize("cicchio", "12345");
			System.out.println("[Size File Storage] cicchio ha " + a + " file salvati");
			
			receive = dropbox.remove("cicchio", "12345", "file1");
			System.out.println("[Remove] cicchio ha eliminato " + receive + " dal suo file storage");
			a = dropbox.getSize("cicchio", "12345");
			System.out.println("[Size File Storage] cicchio ha " + a + " file salvati");
			
		} catch (NullPointerException | UserAlreadyRegisteredException | WeakPasswordException | IllegalUsernameException | UserNotFoundException | WrongPasswordException | NoDataException e) {
			// TODO Auto-generated catch block
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
