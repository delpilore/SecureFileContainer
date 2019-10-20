package source; 

import java.util.Iterator;
import java.util.Scanner;

import source.SecureFileContainer.IllegalSharingException;
import source.SecureFileContainer.IllegalUsernameException;
import source.SecureFileContainer.NoDataException;
import source.SecureFileContainer.UserAlreadyRegisteredException;
import source.SecureFileContainer.UserNotFoundException;
import source.SecureFileContainer.WeakPasswordException;
import source.SecureFileContainer.WrongPasswordException;

// AUTHOR: Lorenzo Del Prete, Corso B, 531417

public class MainClass {
    public static void main(String[] argv) {
        Scanner in = new Scanner(System.in);
        String c;
        String t;
        SecureFileContainer<String> dropbox = null;
        
        System.out.print("Scegliere il testing desiderato:\n\tA\tTesting Automatico\n\tM\tTesting Manuale\n\t");
        t = in.next();
        while (!"A".equals(t) && !"M".equals(t) && !"a".equals(t) && !"m".equals(t)) {
            System.out.print("**Comando non valido**\n\t");
            t = in.next();
        }
        
        System.out.print("Scegliere l'implementazione desiderata per il testing:\n\t1\tImplementazione1 con HashMap\n\t2\tImplementazione2 con ArrayList\n\t");
        c = in.next();
        while (!"1".equals(c) && !"2".equals(c)) {
            System.out.print("**Comando non valido**\n\t");
            c = in.next();
        }
        
        if (c.equals("1")) {
            dropbox = new SecureFileContainer_Impl1<String>();
            System.out.println("E' stata scelta l'implementazione 1! Test in esecuzione con file simulati da dati tipo stringa.");
        } else if (c.equals("2")) {
            dropbox = new SecureFileContainer_Impl2<String>();
            System.out.println("E' stata scelta l'implementazione 2! Test in esecuzione con file simulati da dati tipo stringa.");
        }  

        if (t.equals("M") || t.equals("m")) { // è stato scelto il testing manuale
	        do {
	            System.out.print("Operazioni:\n"
	                    + "\tA: Registrazione di un utente\n"
	                    + "\tN: Numero dei file salvati da un utente\n"
	                    + "\tI: Upload di un file nello storage di un utente\n"
	                    + "\tD: Download di un file dallo storage di un utente\n"
	                    + "\tR: Rimozione di un file dallo storage di un utente\n"
	                    + "\tC: Copia di un file dallo storage di un utente\n"
	                    + "\tSw: Condivisione di un file ad altro utente, in lettura/scrittura\n"
	                    + "\tSr: Condivisione di un file ad un altro utente, in sola lettura\n"
	                    + "\tZ: Stampa dello stato dello storage di un utente\n"
	            		+ "\tX: Chiudi\n\t");
	            c = in.next();
	            
	            switch (c) {
	                case "A":
	                case "a":
	                    String usr, pass;
	                    System.out.print("Nome utente: ");
	                    usr = in.next();
	                    System.out.print("Password: ");
	                    pass = in.next();
	                    
						try {
							dropbox.createUser(usr, pass);
							System.out.println(usr + " registrato correttamente al sistema!");
						} catch (NullPointerException e) {
							System.out.println("[ERRORE]: Passati nome utente o password null");
						} catch (UserAlreadyRegisteredException e) {
							System.out.println("[ERRORE]: Utente " + usr + " già registrato!");
						} catch (WeakPasswordException e) {
							System.out.println("[ERRORE]: La password inserita è troppo corta! Inseriscine una di almeno 5 caratteri!");
						} catch (IllegalUsernameException e) {
							System.out.println("[ERRORE]: L'username è troppo corto! Inseriscine uno di almeno 2 caratteri!");
						}
		                
						break;
	                
	                case "N":
	                case "n":
	                    System.out.print("Nome utente: ");
	                    usr = in.next();
	                    System.out.print("Password: ");
	                    pass = in.next();
	
						try {
							System.out.println("Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, pass));
						} catch (NullPointerException e) {
							System.out.println("[ERRORE]: Passati nome utente o password null");
						} catch (UserNotFoundException e) {
							System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
						} catch (WrongPasswordException e) {
							System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
						}
		            
	                    break;
	                    
	                case "I":
	                case "i":
	                    String data;
	                    System.out.print("Nome utente: ");
	                    usr = in.next();
	                    System.out.print("Password: ");
	                    pass = in.next();
	                    System.out.print("File da inserire: ");
	                    data = in.next();
	
						try {
							dropbox.put(usr, pass, data);
		                    System.out.println(data + " caricato correttamente nello storage dell'utente " + usr);
		                } catch (NullPointerException e) {
		                	System.out.println("[ERRORE]: Passati nome utente, password o file, null");
						} catch (UserNotFoundException e) {
							System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
						} catch (WrongPasswordException e) {
							System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
						}
	     
	                    break;
	                
	                case "D":
	                case "d":
	                    System.out.print("Nome utente: ");
	                    usr = in.next();
	                    System.out.print("Password: ");
	                    pass = in.next();
	                    System.out.print("File da scaricare: ");
	                    data = in.next();
	         
						try {
							System.out.println("Ottenuto " + dropbox.get(usr, pass, data) + " dallo storage di " + usr);
						} catch (NullPointerException e) {
							System.out.println("[ERRORE]: Passati nome utente, password o file, null");
						} catch (UserNotFoundException e) {
							System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
						} catch (WrongPasswordException e) {
							System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
						} catch (NoDataException e) {
							System.out.println("[ERRORE]: Il file " + data + " ricercato, non esiste nel file storage di " + usr);
						}
	         
	                    break;
	                
	                case "R":
	                case "r":
	                    System.out.print("Nome utente: ");
	                    usr = in.next();
	                    System.out.print("Password: ");
	                    pass = in.next();
	                    System.out.print("File da rimuovere: ");
	                    data = in.next();
	                    
						try {
							System.out.println("Rimosso " + dropbox.remove(usr, pass, data) + " dallo storage di " + usr + " e da quello di tutti gli utenti a cui era stato eventualmente condiviso!");
						} catch (NullPointerException e) {
							System.out.println("[ERRORE]: Passati nome utente, password o file, null");
						} catch (UserNotFoundException e) {
							System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
						} catch (WrongPasswordException e) {
							System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
						} catch (NoDataException e) {
							System.out.println("[ERRORE]: Il file " + data + " da rimuovere, non esiste nel file storage di " + usr);
						}
	
	                    break;
	                    
	                case "C":
	                case "c":
	                    System.out.print("Nome utente: ");
	                    usr = in.next();
	                    System.out.print("Password: ");
	                    pass = in.next();
	                    System.out.print("File da copiare: ");
	                    data = in.next();
	                    
						try {
							dropbox.copy(usr, pass, data);
							System.out.println("Copiato " + data + " nel file storage di " + usr);
						} catch (NullPointerException e) {
							System.out.println("[ERRORE]: Passati nome utente, password o file, null");
						} catch (UserNotFoundException e) {
							System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
						} catch (WrongPasswordException e) {
							System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
						} catch (NoDataException e) {
							System.out.println("[ERRORE]: Il file " + data + " da copiare, non esiste nel file storage di " + usr);
						}
						
	                    break;
	                    
	                case "Sw":
	                case "sw":
	                    String other;
	                    System.out.print("Nome utente: ");
	                    usr = in.next();
	                    System.out.print("Password: ");
	                    pass = in.next();
	                    System.out.print("File da condividere in lettura/scrittura: ");
	                    data = in.next();
	                    System.out.print("Utente a cui condividere in lettura/scrittura il file " + data + ": ");
	                    other = in.next();
	
	          
						try {
							dropbox.shareW(usr, pass, other, data);
							System.out.println("Condiviso in lettura/scrittura il file " + data + " nel file storage di " + other);
						} catch (NullPointerException e) {
							System.out.println("[ERRORE]: Passati nomi utente, password o file, null");
						} catch (UserNotFoundException e) {
							System.out.println("[ERRORE]: Utente " + usr + " o utente " + other + " non trovato!");
						} catch (WrongPasswordException e) {
							System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
						} catch (NoDataException e) {
							System.out.println("[ERRORE]: Il file " + data + " da condividere in lettura/scrittura a " + other + " non esiste nel file storage di " + usr);
						} catch (IllegalSharingException e) {
							System.out.println("[ERRORE]: Condivisione non valida! Stai tentando di condividere un file a te stesso, o un file non di tua proprietà!");
						}
						
	                    break;
	                    
	                case "Sr":
	                case "sr":
	                    System.out.print("Nome utente: ");
	                    usr = in.next();
	                    System.out.print("Password: ");
	                    pass = in.next();
	                    System.out.print("File da condividere in sola lettura: ");
	                    data = in.next();
	                    System.out.print("Utente a cui condividere in sola lettura il file " + data + ": ");
	                    other = in.next();
	
	          
						try {
							dropbox.shareR(usr, pass, other, data);
							System.out.println("Condiviso in sola lettura il file " + data + " nel file storage di " + other);
						} catch (NullPointerException e) {
							System.out.println("[ERRORE]: Passati nomi utente, password o file, null");
						} catch (UserNotFoundException e) {
							System.out.println("[ERRORE]: Utente " + usr + " o utente " + other + " non trovato!");
						} catch (WrongPasswordException e) {
							System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
						} catch (NoDataException e) {
							System.out.println("[ERRORE]: Il file " + data + " da condividere in sola lettura a " + other + " non esiste nel file storage di " + usr);	
						} catch (IllegalSharingException e) {
							System.out.println("[ERRORE]: Condivisione non valida! Stai tentando di condividere un file a te stesso, o un file non di tua proprietà!");
						}
	
	                    break;
	                    
	                case "Z":
	                case "z":
	                    System.out.print("Nome utente: ");
	                    usr = in.next();
	                    System.out.print("Password: ");
	                    pass = in.next();
	                    
						Iterator<String> i = null;
						try {
							i = dropbox.getIterator(usr, pass);
							System.out.println(usr+ " ha i seguenti file salvati: ");
							
		                    String temp;
		                    while ( i!=null &&  i.hasNext()) {
		                    	temp = i.next();
		                    	System.out.println("- " + temp);
		                    }
						} catch (NullPointerException e) {
							System.out.println("[ERRORE]: Passati nome utente o password null");
						} catch (UserNotFoundException e) {
							System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
						} catch (WrongPasswordException e) {
							System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
						} catch (NoDataException e) {
							System.out.println("[ERRORE]: Lo storage di " + usr + " è vuoto!");
						}
						
	                    break;
	                    
	            }
	
	        } while (!"X".equals(c) && !"x".equals(c));
        }
        else { //testing automatico
        	in.close();
        	
    		String usr = null;
    		String psw = "12345";
    		String data;
    		
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONE createUser");
        	System.out.println("");
    		
        	try {
        		usr = "pippo";
        		
        		System.out.println("Provo a registrare " + usr + " con password " + psw);
				dropbox.createUser(usr, psw);
				System.out.println("[REGISTERD] " + usr + " registrato correttamente al sistema!");
				
				System.out.println("");
				
				usr = "pluto";
        		System.out.println("Provo a registrare " + usr + " con password " + psw);
	        	dropbox.createUser(usr, psw);
	        	System.out.println("[REGISTERD] " + usr + " registrato correttamente al sistema!");
	        	
	        	System.out.println("");
	        	
	        	usr = "topolino";
        		System.out.println("Provo a registrare " + usr + " con password " + psw);
	        	dropbox.createUser(usr, psw);
	        	System.out.println("[REGISTERD] " + usr + " registrato correttamente al sistema!");
	        	
	        	System.out.println("");
	        	
	        	usr = "topolino";
        		System.out.println("Provo a registrare " + usr + " con password " + psw);
	        	dropbox.createUser(usr, psw);
	        	System.out.println("[REGISTERD] " + usr + " registrato correttamente al sistema!");
	        	
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserAlreadyRegisteredException e) {
				System.out.println("[ERRORE]: Utente " + usr + " già registrato!");
			} catch (WeakPasswordException e) {
				System.out.println("[ERRORE]: La password inserita è troppo corta! Inseriscine una di almeno 5 caratteri!");
			} catch (IllegalUsernameException e) {
				System.out.println("[ERRORE]: L'username è troppo corto! Inseriscine uno di almeno 2 caratteri!");
			}
        	
        	System.out.println("");
        	
        	usr = "a";
    		System.out.println("Provo a registrare " + usr + " con password " + psw);
        	try {
				dropbox.createUser(usr, psw);
	        	System.out.println("[REGISTERD] " + usr + " registrato correttamente al sistema!");
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserAlreadyRegisteredException e) {
				System.out.println("[ERRORE]: Utente " + usr + " già registrato!");
			} catch (WeakPasswordException e) {
				System.out.println("[ERRORE]: La password inserita è troppo corta! Inseriscine una di almeno 5 caratteri!");
			} catch (IllegalUsernameException e) {
				System.out.println("[ERRORE]: L'username è troppo corto! Inseriscine uno di almeno 2 caratteri!");
			}

        	System.out.println("");
        	
        	usr = "minnie";
        	psw = "123";
    		System.out.println("Provo a registrare " + usr + " con password " + psw);
        	try {
				dropbox.createUser(usr, psw);
	        	System.out.println("[REGISTERD] " + usr + " registrato correttamente al sistema!");
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserAlreadyRegisteredException e) {
				System.out.println("[ERRORE]: Utente " + usr + " già registrato!");
			} catch (WeakPasswordException e) {
				System.out.println("[ERRORE]: La password inserita è troppo corta! Inseriscine una di almeno 5 caratteri!");
			} catch (IllegalUsernameException e) {
				System.out.println("[ERRORE]: L'username è troppo corto! Inseriscine uno di almeno 2 caratteri!");
			}
        	
        	System.out.println("");
        	
        	usr = null;
        	psw = null;
    		System.out.println("Provo a registrare " + usr + " con password " + psw);
        	try {
				dropbox.createUser(usr, psw);
	        	System.out.println("[REGISTERD] " + usr + " registrato correttamente al sistema!");
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserAlreadyRegisteredException e) {
				System.out.println("[ERRORE]: Utente " + usr + " già registrato!");
			} catch (WeakPasswordException e) {
				System.out.println("[ERRORE]: La password inserita è troppo corta! Inseriscine una di almeno 5 caratteri!");
			} catch (IllegalUsernameException e) {
				System.out.println("[ERRORE]: L'username è troppo corto! Inseriscine uno di almeno 2 caratteri!");
			}
        	
        	
        	// Fatti questi test di iscrizioni dovrebbero risultare 3 iscritti (pippo, pluto e topolino) e 4 errori
        	
        	System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbero risultare 3 utenti iscritti correttamente e 4 errori");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONE getSize");
        	System.out.println("");
        	
        	// Test di inserimento file e recupero dimensione file storage
        	
        	usr = "pippo";
        	psw = "12345";
        	
        	try {
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "pluto";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "topolino";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "minnie";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
        	
        	System.out.println("");
        	
			usr = "topolino";
			psw = "123";
    		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
			try {
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
			
        	System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbe risultare dimensione 0 per i 3 utenti iscritti (pippo, pluto, topolino) e due errori");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONE put");
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	data = "FILE1";
        	
			try {
				System.out.println("Provo ad inserire il file " + data + " nel file storage di " + usr + " con password " + psw);
				dropbox.put(usr, psw, data);
                System.out.println("[UPLOAD] " + data + " caricato correttamente nello storage dell'utente " + usr);
                
                System.out.println("");
                
                usr = "pluto";
                data ="FILE2";
        		System.out.println("Provo ad inserire il file " + data + " nel file storage di " + usr + " con password " + psw);
				dropbox.put(usr, psw, data);
                System.out.println("[UPLOAD] " + data + " caricato correttamente nello storage dell'utente " + usr);
                
                System.out.println("");
                
                usr = "pluto";
                data = "FILE3";
        		System.out.println("Provo ad inserire il file " + data + " nel file storage di " + usr + " con password " + psw);
				dropbox.put(usr, psw, data);
                System.out.println("[UPLOAD] " + data + " caricato correttamente nello storage dell'utente " + usr);
                
                System.out.println("");
                
                usr = "topolino";
                data = "FILE1";
        		System.out.println("Provo ad inserire il file " + data + " nel file storage di " + usr + " con password " + psw);
				dropbox.put(usr, psw, data);
                System.out.println("[UPLOAD] " + data + " caricato correttamente nello storage dell'utente " + usr);
                
                System.out.println("");
                
                usr = "topolino";
                psw = "123";
                data = "file4";
        		System.out.println("Provo ad inserire il file " + data + " nel file storage di " + usr + " con password " + psw);
				dropbox.put(usr, psw, data);
                System.out.println("[UPLOAD] " + data + " caricato correttamente nello storage dell'utente " + usr);
                
            } catch (NullPointerException e) {
            	System.out.println("[ERRORE]: Passati nome utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
			
			System.out.println("");
			
            usr = "topolino";
            psw = "123";
            data = null;
    		System.out.println("Provo ad inserire il file " + data + " nel file storage di " + usr + " con password " + psw);
			try {
				dropbox.put(usr, psw, data);
			    System.out.println("[UPLOAD] " + data + " caricato correttamente nello storage dell'utente " + usr);
            } catch (NullPointerException e) {
            	System.out.println("[ERRORE]: Passati nome utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
			
        	System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbero risultare: 1 inserimento per pippo (FILE1), 2 per pluto (FILE2 e FILE3) , 1 per topolino (FILE1) e 2 errori");
        	System.out.println("- pippo a questo punto dovrebbe avere 1 file: FILE1");
        	System.out.println("- pluto a questo punto dovrebbe avere 2 file: FILE2, FILE3");
        	System.out.println("- topolino a questo punto dovrebbe avere 1 file: FILE1");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI getSize e getIterator (per controllare che tutto torni)");
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	try {
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "pluto";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "topolino";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
					
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
        	
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	Iterator<String> i = null;
        	
			try {
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                String temp;
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
                System.out.println("");
                
                usr = "pluto";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
                System.out.println("");
                
                usr="topolino";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
              
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Lo storage di " + usr + " è vuoto!");
			}
			
        	System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbe trasparire che il sistema risulta consistente con quanto fatto con la put precedentemente");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI copy");
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	data = "FILE1";
			try {
	    		System.out.println("Provo a copiare il file " + data + " contenuto nel file storage di " + usr + " con password " + psw);
				dropbox.copy(usr, psw, data);
				System.out.println("[COPY] Copiato " + data + " nel file storage di " + usr);
				
				System.out.println("");
				
				usr="pluto";
				data ="FILE2";
	    		System.out.println("Provo a copiare il file " + data + " contenuto nel file storage di " + usr + " con password " + psw);
				dropbox.copy(usr, psw, data);
				System.out.println("[COPY] Copiato " + data + " nel file storage di " + usr);
				
				System.out.println("");
				
				usr ="topolino";
				data ="FILE1";
	    		System.out.println("Provo a copiare il file " + data + " contenuto nel file storage di " + usr + " con password " + psw);
				dropbox.copy(usr, psw, data);
				System.out.println("[COPY] Copiato " + data + " nel file storage di " + usr);
				
				System.out.println("");
				
				usr ="pippo";
				data="file7";
	    		System.out.println("Provo a copiare il file " + data + " contenuto nel file storage di " + usr + " con password " + psw);
				dropbox.copy(usr, psw, data);
				System.out.println("[COPY] Copiato " + data + " nel file storage di " + usr);
				
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Il file " + data + " da copiare, non esiste nel file storage di " + usr);
			}
			
        	System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbero risultare: 1 file copiato per pippo (FILE1), 1 file copiato per pluto (FILE2) , 1 file copiato per topolino (FILE1) e 1 errore");
        	System.out.println("- pippo a questo punto dovrebbe avere 2 file: FILE1, FILE1");
        	System.out.println("- pluto a questo punto dovrebbe avere 3 file: FILE2, FILE3, FILE2");
        	System.out.println("- topolino a questo punto dovrebbe avere 2 file: FILE1, FILE1");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI getSize e getIterator (per controllare che tutto torni)");
        	System.out.println("");
			
        	usr = "pippo";
        	psw = "12345";
        	try {
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "pluto";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "topolino";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
					
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	i = null;
        	
			try {
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                String temp;
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
                System.out.println("");
                
                usr = "pluto";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
                System.out.println("");
                
                usr="topolino";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Lo storage di " + usr + " è vuoto!");
			}
			
			System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbe trasparire che il sistema risulta consistente con quanto fatto con la copy precedentemente");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI remove");
        	System.out.println("");
			
			
			usr ="pippo";
			psw ="12345";
			data ="FILE1";
			
			try {
				System.out.println("Provo a rimuovere il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[REMOVE] Rimosso " + dropbox.remove(usr, psw, data) + " dallo storage di " + usr + " e da quello di tutti gli utenti a cui era stato eventualmente condiviso!");
				
				System.out.println("");
				
				System.out.println("Provo a rimuovere il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[REMOVE] Rimosso " + dropbox.remove(usr, psw, data) + " dallo storage di " + usr + " e da quello di tutti gli utenti a cui era stato eventualmente condiviso!");
				
				System.out.println("");
				
				usr ="pluto";
				data = "FILE3";
				System.out.println("Provo a rimuovere il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[REMOVE] Rimosso " + dropbox.remove(usr, psw, data) + " dallo storage di " + usr + " e da quello di tutti gli utenti a cui era stato eventualmente condiviso!");
				
				System.out.println("");
				
				psw = "2172773";
				System.out.println("Provo a rimuovere il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[REMOVE] Rimosso " + dropbox.remove(usr, psw, data) + " dallo storage di " + usr + " e da quello di tutti gli utenti a cui era stato eventualmente condiviso!");
			
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Il file " + data + " da rimuovere, non esiste nel file storage di " + usr);
			}
			
			System.out.println("");
			
			usr = "topolino";
			psw = "12345";
			data ="file9";
			
			System.out.println("Provo a rimuovere il file " + data + " dal file storage di " + usr + " con password " + psw);
			try {
				System.out.println("[REMOVE] Rimosso " + dropbox.remove(usr, psw, data) + " dallo storage di " + usr + " e da quello di tutti gli utenti a cui era stato eventualmente condiviso!");
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Il file " + data + " da rimuovere, non esiste nel file storage di " + usr);
			}
			
        	System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbero risultare: 2 file rimossi per pippo (FILE1 e FILE1), 1 file rimosso per pluto (FILE3) , 0 rimossi per topolino e 2 errori");
        	System.out.println("- pippo a questo punto dovrebbe avere 0 file");
        	System.out.println("- pluto a questo punto dovrebbe avere 2 file: FILE2, FILE2");
        	System.out.println("- topolino a questo punto dovrebbe avere 2 file: FILE1, FILE1");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI getSize e getIterator (per controllare che tutto torni)");
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	try {
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "pluto";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "topolino";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
					
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
        	
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	i = null;
        	
			try {
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                String temp;
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Lo storage di " + usr + " è vuoto!");
			}
			
			System.out.println("");
			
			try {
				String temp;
                usr = "pluto";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
                System.out.println("");
                
                usr="topolino";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Lo storage di " + usr + " è vuoto!");
			}
			
			System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbe trasparire che il sistema risulta consistente con quanto fatto con la remove precedentemente");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI shareW");
        	System.out.println("");
        	
  
        	usr = "pluto";
        	psw = "12345";
        	String other = "pippo";
        	data = "FILE2";
			try {
				System.out.println("Provo a condividere il file " + data + " dal file storage di " + usr + " con password " + psw + " al file storage di " + other + " in lettura/scrittura");
				dropbox.shareW(usr, psw, other, data);
				System.out.println("[SHARED_W] Condiviso in lettura/scrittura il file " + data + " nel file storage di " + other);
				
				System.out.println("");
				
	        	other = "topolino";
				System.out.println("Provo a condividere il file " + data + " dal file storage di " + usr + " con password " + psw + " al file storage di " + other + " in lettura/scrittura");
				dropbox.shareW(usr, psw, other, data);
				System.out.println("[SHARED_W] Condiviso in lettura/scrittura il file " + data + " nel file storage di " + other);
				
				System.out.println("");
				
				usr ="pippo";
				data = "FILE2";
				other = "topolino";
				System.out.println("Provo a condividere il file " + data + " dal file storage di " + usr + " con password " + psw + " al file storage di " + other + " in lettura/scrittura");
				dropbox.shareW(usr, psw, other, data);
				System.out.println("[SHARED_W] Condiviso in lettura/scrittura il file " + data + " nel file storage di " + other);
				
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nomi utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " o utente " + other + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Il file " + data + " da condividere in lettura/scrittura a " + other + " non esiste nel file storage di " + usr);
			} catch (IllegalSharingException e) {
				System.out.println("[ERRORE]: Condivisione non valida! Stai tentando di condividere un file a te stesso, o un file non di tua proprietà!");
			}
			
        	System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbero risultare: 1 file condiviso a pippo da pluto (FILE2), 1 file condiviso a topolino da pluto (FILE2) e 1 errore per tentata condivisione di un file non di proprietà");
        	System.out.println("- pippo a questo punto dovrebbe avere 1 file: FILE2 (condiviso da pluto in lettura/scrittura");
        	System.out.println("- pluto a questo punto dovrebbe avere 2 file: FILE2, FILE2");
        	System.out.println("- topolino a questo punto dovrebbe avere 3 file: FILE1, FILE1, FILE2 (condiviso da pluto in lettura/scrittura)");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI getSize, getIterator (per controllare che tutto torni) + operazione get per controllare se i permessi delle condivisioni sono come previsti");
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	try {
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "pluto";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "topolino";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
					
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
        	
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	i = null;
        	
			try {
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                String temp;
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
                System.out.println("");
                
                usr = "pluto";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
                System.out.println("");
                
                usr="topolino";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Lo storage di " + usr + " è vuoto!");
			}
			
			System.out.println("");
			
			usr ="pippo";
			data = "FILE2";
			try {
				System.out.println("Provo a scaricare il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[DOWNLOAD] Ottenuto " + dropbox.get(usr, psw, data) + " dallo storage di " + usr);
				
				System.out.println("");
				
				usr="topolino";
				System.out.println("Provo a scaricare il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[DOWNLOAD] Ottenuto " + dropbox.get(usr, psw, data) + " dallo storage di " + usr);

			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Il file " + data + " ricercato, non esiste nel file storage di " + usr);
			}
			
			System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbe trasparire che il sistema risulta consistente con quanto fatto con la shareW precedentemente");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI shareR");
        	System.out.println("");
        	
        	
        	////////////////////SHARING IN LETTURA///////////////
			
			
        	usr = "pluto";
        	psw = "12345";
        	other = "pippo";
        	data = "FILE2";
			try {
				System.out.println("Provo a condividere il file " + data + " dal file storage di " + usr + " con password " + psw + " al file storage di " + other + " in sola lettura");
				dropbox.shareR(usr, psw, other, data);
				System.out.println("[SHARED_R] Condiviso in sola lettura il file " + data + " nel file storage di " + other);
				
				System.out.println("");
				
	        	other = "topolino";
				System.out.println("Provo a condividere il file " + data + " dal file storage di " + usr + " con password " + psw + " al file storage di " + other + " in sola lettura");
				dropbox.shareR(usr, psw, other, data);
				System.out.println("[SHARED_R] Condiviso in sola lettura il file " + data + " nel file storage di " + other);
				
				System.out.println("");
				
				usr ="pippo";
				data = "FILE2";
				other = "topolino";
				System.out.println("Provo a condividere il file " + data + " dal file storage di " + usr + " con password " + psw + " al file storage di " + other + " in sola lettura");
				dropbox.shareR(usr, psw, other, data);
				System.out.println("[SHARED_R] Condiviso in sola lettura il file " + data + " nel file storage di " + other);
				
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nomi utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " o utente " + other + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Il file " + data + " da condividere in sola lettura a " + other + " non esiste nel file storage di " + usr);	
			} catch (IllegalSharingException e) {
				System.out.println("[ERRORE]: Condivisione non valida! Stai tentando di condividere un file a te stesso, o un file non di tua proprietà!");
			}
			
        	System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbero risultare inalterati i files posseduti dagli utenti iscritti, VENGONO SOLO AGGIORNATI I PERMESSI D'ACCESSO, + 1 errore per tentata condivisione di un file non di proprietà");
        	System.out.println("- pippo a questo punto dovrebbe avere 1 file: FILE2 (condiviso da pluto in SOLA LETTURA -> PERMESSO AGGIORNATO RISPETTO ALL'OPERAZIONE DI SHAREW PRECEDENTE)");
        	System.out.println("- pluto a questo punto dovrebbe avere 2 file: FILE2, FILE2");
        	System.out.println("- topolino a questo punto dovrebbe avere 3 file: FILE1, FILE1, FILE2 (condiviso da pluto in SOLA LETTURA -> PERMESSO AGGIORNATO RISPETTO ALL'OPERAZIONE DI SHAREW PRECEDENTE)");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI getSize, getIterator (per controllare che tutto torni) + operazione get per controllare se i permessi delle condivisioni sono come previsti");
        	System.out.println("");
			
        	usr = "pippo";
        	psw = "12345";
        	try {
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "pluto";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "topolino";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
					
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
        	
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	i = null;
        	
			try {
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                String temp;
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
                System.out.println("");
                
                usr = "pluto";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
                System.out.println("");
                
                usr="topolino";
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Lo storage di " + usr + " è vuoto!");
			}
			
			System.out.println("");
			
			usr ="pippo";
			data = "FILE2";
			try {
				System.out.println("Provo a scaricare il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[DOWNLOAD] Ottenuto " + dropbox.get(usr, psw, data) + " dallo storage di " + usr);
				
				System.out.println("");
				
				usr="topolino";
				System.out.println("Provo a scaricare il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[DOWNLOAD] Ottenuto " + dropbox.get(usr, psw, data) + " dallo storage di " + usr);

			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Il file " + data + " ricercato, non esiste nel file storage di " + usr);
			}
			
			System.out.println("");
        	System.out.println("***ASSERZIONE: da questo test dovrebbe trasparire che il sistema risulta consistente con quanto fatto con la shareR precedentemente");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONE remove (per testare la rimozione dei file condivisi)");
        	System.out.println("");
			
			try {
				usr = "pluto";
				data = "FILE2";
				System.out.println("Provo a rimuovere il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[REMOVE] Rimosso " + dropbox.remove(usr, psw, data) + " dallo storage di " + usr + " e da quello di tutti gli utenti a cui era stato eventualmente condiviso!");
				
				System.out.println("");
				
				usr ="pluto";
				data = "FILE2";
				System.out.println("Provo a rimuovere il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[REMOVE] Rimosso " + dropbox.remove(usr, psw, data) + " dallo storage di " + usr + " e da quello di tutti gli utenti a cui era stato eventualmente condiviso!");

			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Il file " + data + " da rimuovere, non esiste nel file storage di " + usr);
			}
			
        	System.out.println("");
        	System.out.println("***ASSERZIONE: visto che pluto ha eliminato entrambi i suoi FILE2 e uno dei due l'aveva condiviso a pippo e topolino, ora questi ultimi non se lo troveranno più nel loro file storage");
        	System.out.println("- pippo a questo punto dovrebbe avere 0 file");
        	System.out.println("- pluto a questo punto dovrebbe avere 0 file");
        	System.out.println("- topolino a questo punto dovrebbe avere 2 file: FILE1, FILE1");
        	System.out.println("");
        	System.out.println("-----------------------------------------------------------------");
        	System.out.println("");
        	System.out.println("\tINIZIO TESTING OPERAZIONI getSize, getIterator e get finale (per controllare che tutto torni)");
        	System.out.println("");
        	
        	usr = "pippo";
        	psw = "12345";
        	try {
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "pluto";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
				
				System.out.println("");
				
				usr = "topolino";
        		System.out.println("Provo ad ottenere la dimensione del file storage di " + usr + " con password " + psw);
				System.out.println("[NUMFILES] Numero di files salvati dall'utente " + usr + ": " + dropbox.getSize(usr, psw));
					
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			}
        	
        	System.out.println("");
        	
        	usr = "topolino";
        	psw = "12345";
        	i = null;
        	
			try {
				System.out.println("Provo ad ottenere il resosconto dei file caricati da " + usr + " con password " + psw);
				i = dropbox.getIterator(usr, psw);
				System.out.println("[RESOCONTO FILES] " + usr+ " ha i seguenti file salvati: ");
				
                String temp;
                while ( i!=null &&  i.hasNext()) {
                	temp = i.next();
                	System.out.println("- " + temp);
                }
                
			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente o password null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Lo storage di " + usr + " è vuoto!");
			}
			
			System.out.println("");
			
			usr ="topolino";
			data = "FILE1";
			try {
				System.out.println("Provo a scaricare il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[DOWNLOAD] Ottenuto " + dropbox.get(usr, psw, data) + " dallo storage di " + usr);
				
				System.out.println("");
				
				System.out.println("Provo a scaricare il file " + data + " dal file storage di " + usr + " con password " + psw);
				System.out.println("[DOWNLOAD] Ottenuto " + dropbox.get(usr, psw, data) + " dallo storage di " + usr);

			} catch (NullPointerException e) {
				System.out.println("[ERRORE]: Passati nome utente, password o file, null");
			} catch (UserNotFoundException e) {
				System.out.println("[ERRORE]: Utente " + usr + " non trovato!");
			} catch (WrongPasswordException e) {
				System.out.println("[ERRORE]: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
			} catch (NoDataException e) {
				System.out.println("[ERRORE]: Il file " + data + " ricercato, non esiste nel file storage di " + usr);
			}
			
			System.out.println("");
			System.out.println("----- FINE TESTING AUTOMATICO -----");
			
        }
        
        
        
    if(t.equals("M") || t.equals("m"))
    	in.close();
    }
}