package source; 

import java.util.Iterator;
import java.util.Scanner;

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
        SecureFileContainer<String> dropbox = null;
        
        
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

        do {
            System.out.print("Operazioni:\n"
                    + "\tA\tRegistrazione di un utente\n"
                    + "\tN\tNumero dei file salvati da un utente\n"
                    + "\tI\tUpload di un file nello storage di un utente\n"
                    + "\tD\tDownload di un file dallo storage di un utente\n"
                    + "\tR\tRimozione di un file dallo storage di un utente\n"
                    + "\tC\tCopia di un file dallo storage di un utente\n"
                    + "\tSw\tCondivisione di un file ad altro utente, in lettura/scrittura\n"
                    + "\tSr\tCondivisione di un file ad un altro utente, in sola lettura\n"
                    + "\tZ\tStampa dello stato dello storage di un utente\n"
            		+ "\tX\tChiudi\n\t");
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
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserAlreadyRegisteredException e) {
						System.out.println("ERRORE: Utente " + usr + " già registrato!");
					} catch (WeakPasswordException e) {
						System.out.println("ERRORE: La password inserita è troppo corta! Inseriscine una di almeno 5 caratteri!");
					} catch (IllegalUsernameException e) {
						System.out.println("ERRORE: L'username è troppo corto! Inseriscine uno di almeno 2 caratteri!");
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
                    pass = in.next();
                    System.out.print("File da inserire: ");
                    data = in.next();

					try {
						dropbox.put(usr, pass, data);
	                    System.out.println(data + " caricato correttamente nello storage dell'utente " + usr);
	                } catch (NullPointerException e) {
	                	System.out.println("ERRORE: Passati nome utente, password o file, null");
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
                    pass = in.next();
                    System.out.print("File da scaricare: ");
                    data = in.next();
         
					try {
						System.out.println("Ottenuto " + dropbox.get(usr, pass, data) + " dallo storage di " + usr);
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente, password o file, null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " ricercato, non esiste nel file storage di " + usr);
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
						System.out.println("Rimosso " + dropbox.remove(usr, pass, data) + " dallo storage di " + usr);
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente, password o file, null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " da rimuovere, non esiste nel file storage di " + usr);
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
						System.out.println("ERRORE: Passati nome utente, password o file, null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " da copiare, non esiste nel file storage di " + usr);
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
						System.out.println("ERRORE: Passati nomi utente, password o file, null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " o utente " + other + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " da condividere in lettura/scrittura a " + other + " non esiste nel file storage di " + usr);
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
						System.out.println("ERRORE: Passati nomi utente, password o file, null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " o utente " + other + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Il file " + data + " da condividere in sola lettura a " + other + " non esiste nel file storage di " + usr);	
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
	                    	System.out.println("-" + temp);
	                    }
					} catch (NullPointerException e) {
						System.out.println("ERRORE: Passati nome utente o password null");
					} catch (UserNotFoundException e) {
						System.out.println("ERRORE: Utente " + usr + " non trovato!");
					} catch (WrongPasswordException e) {
						System.out.println("ERRORE: L'utente " + usr + " risulta registrato ma la password non corrisponde!");
					} catch (NoDataException e) {
						System.out.println("ERRORE: Lo storage di " + usr + " è vuoto!");
					}
					
                    break;
                    
            }

        } while (!"X".equals(c) && !"x".equals(c));
    
    in.close();
    }
}