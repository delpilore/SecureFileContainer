package source; 

import java.util.Iterator;
import java.util.Scanner;

import source.SecureFileContainer.IllegalUsernameException;
import source.SecureFileContainer.NoDataException;
import source.SecureFileContainer.UserAlreadyRegisteredException;
import source.SecureFileContainer.UserNotFoundException;
import source.SecureFileContainer.WeakPasswordException;
import source.SecureFileContainer.WrongPasswordException;

//AUTHOR: Lorenzo Del Prete, Corso B, 531417

public class MainClass {
    public static void main(String[] argv) {
        Scanner in = new Scanner(System.in);
        String c;
        SecureFileContainer<String> dropbox = null;
        
        
        System.out.print("Scegliere l'implementazione desiderata per il testing::\n\t1\tImplementazione1 con HashMap\n\t2\tImplementazione2 con ArrayList\n\t");
        c = in.next();
        while (!"1".equals(c) && !"2".equals(c)) {
            System.out.print("**Comando non valido**\n\t");
            c = in.next();
        }
        
        if (c.equals("1")) {
            dropbox = new SecureFileContainer_Impl1<String>();
        } else if (c.equals("2")) {
            dropbox = new SecureFileContainer_Impl2<String>();
        }

        System.out.println("Test in esecuzione con file di tipo stringa.");

        do {
            System.out.print("Operazioni:\n"
                    + "\tA\tRegistrazione di un utente\n"
                    + "\tN\tNumero dei file salvati da un utente\n"
                    + "\tI\tUpload di un file nella collezione di un utente\n"
                    + "\tD\tDownload di un file dalla collezione di un utente\n"
                    + "\tR\tRimozione di un file dalla collezione di un utente\n"
                    + "\tC\tCopia di un file nella collezione di un utente\n"
                    + "\tSw\tCondivisione di un file ad altro utente, in lettura/scrittura\n"
                    + "\tSr\tCondivisione di un file ad un altro utente, in sola lettura\n"
                    + "\tZ\tStampa dello stato della collezione di un utente\n"
            		+ "\tX\tChiudi\n\t");
            c = in.next();
            
            switch (c) {
                case "A":
                case "a":
                    String usr, pwd;
                    System.out.print("Nome utente: ");
                    usr = in.next();
                    System.out.print("Password: ");
                    pwd = in.next();
                    
					try {
						dropbox.createUser(usr, pwd);
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserAlreadyRegisteredException e) {
						System.out.println("ERRORE: Utente " + usr + " già registrato!");
					} catch (WeakPasswordException e) {
						System.out.println("ERRORE: La password inserita è troppo corta! Inseriscine una da almeno 5 caratteri!");
					} catch (IllegalUsernameException e) {
						System.out.println("ERRORE: L'username è troppo corto! Inseriscine uno da almeno 2 caratteri!");
					}
	                
					break;
                
                case "N":
                case "n":
                    System.out.print("Nome utente: ");
                    usr = in.next();
                    System.out.print("Password: ");
                    pwd = in.next();

					try {
						System.out.println("Dimensione della collezione dell'utente " + usr + ": " + dropbox.getSize(usr, pwd));
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					}
	            
                    break;
                    
                case "I":
                case "i":
                    String data;
                    System.out.print("Nome utente: ");
                    usr = in.next();
                    System.out.print("Password: ");
                    pwd = in.next();
                    System.out.print("File da inserire: ");
                    data = in.next();

					try {
						if (dropbox.put(usr, pwd, data)) {
	                        System.out.println(data + " inserito correttamente nella collezione dell'utente " + usr);
	                    } else {
	                    	System.out.println(data + " NON è stato inserito nella collezione dell'utente " + usr);
	                    }
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					}
     
                    break;
                
                case "D":
                case "d":
                    System.out.print("Nome utente: ");
                    usr = in.next();
                    System.out.print("Password: ");
                    pwd = in.next();
                    System.out.print("File da scaricare: ");
                    data = in.next();
         
					try {
						System.out.println("Ottenuto " + dropbox.get(usr, pwd, data) + " dalla collezione di " + usr);
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " ricercato, non esiste nella collezione di " + usr);
					}
         
                    break;
                
                case "R":
                case "r":
                    System.out.print("Nome utente: ");
                    usr = in.next();
                    System.out.print("Password: ");
                    pwd = in.next();
                    System.out.print("File da rimuovere: ");
                    data = in.next();
                    
					try {
						System.out.println("Rimosso " + dropbox.remove(usr, pwd, data) + " dalla collezione di " + usr);
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " da rimuovere, non esiste nella collezione di " + usr);
					}

                    break;
                    
                case "C":
                case "c":
                    System.out.print("Nome utente: ");
                    usr = in.next();
                    System.out.print("Password: ");
                    pwd = in.next();
                    System.out.print("Stringa da copiare: ");
                    data = in.next();
                    
					try {
						dropbox.copy(usr, pwd, data);
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " da copiare, non esiste nella collezione di " + usr);
					}
					
                    System.out.println("Copiato " + data + " nella collezione di " + usr);

                    break;
                    
                case "Sw":
                case "sw":
                    String other;
                    System.out.print("Nome utente: ");
                    usr = in.next();
                    System.out.print("Password: ");
                    pwd = in.next();
                    System.out.print("File da condividere in lettura/scrittura: ");
                    data = in.next();
                    System.out.print("Utente a cui condividere in lettura/scrittura il file " + data + ": ");
                    other = in.next();

          
					try {
						dropbox.shareW(usr, pwd, other, data);
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " o utente " + other + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " da condividere in lettura/scrittura a " + other + " non esiste nella collezione di " + usr);
					}
					
                    System.out.println("Condiviso in lettura/scrittura il file " + data + " nella collezione di " + other);

                    break;
                    
                case "Sr":
                case "sr":
                    System.out.print("Nome utente: ");
                    usr = in.next();
                    System.out.print("Password: ");
                    pwd = in.next();
                    System.out.print("File da condividere in sola lettura: ");
                    data = in.next();
                    System.out.print("Utente a cui condividere in sola lettura il file " + data + ": ");
                    other = in.next();

          
					try {
						dropbox.shareR(usr, pwd, other, data);
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " o utente " + other + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " da condividere in sola lettura a " + other + " non esiste nella collezione di " + usr);
					}
					
                    System.out.println("Condiviso in sola lettura il file " + data + " nella collezione di " + other);

                    break;
                    
                case "Z":
                case "z":
                    System.out.print("Nome utente: ");
                    usr = in.next();
                    System.out.print("Password: ");
                    pwd = in.next();
                    
					Iterator<String> i = null;
					try {
						i = dropbox.getIterator(usr, pwd);
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: " + usr + " non ha file salvati!");
					}
					
					System.out.print(usr+ " ha i seguenti file salvati: ");
					
                    String temp;
                    while ( i!=null &&  i.hasNext()) {
                    	temp = i.next();
                    	System.out.print(temp+"|");
                    	
                    }
                    
                    System.out.println("");
    
                    break;
                    
            }

        } while (!"X".equals(c) && !"x".equals(c));
    
    in.close();
    }
}