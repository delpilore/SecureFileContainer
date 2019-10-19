package source;

import java.util.Iterator;

// AUTHOR: Lorenzo Del Prete, Corso B, 531417

public interface File<E> {
	
	/*
	 * OVERVIEW: L'oggetto di tipo File<E> rappresenta un tipo di dato astratto volto a contenere E e le informazioni di accesso proprie di E
	 * 
	 * TYPICAL ELEMENT: <Dato E, Owner, SharedW, SharedR> 
	 * 					 Dato E: il dato E da incapsulare
	 * 					 Owner: il proprietario di Dato E
	 * 					 SharedW: l'elenco di utenti a cui è consentito l'accesso in lettura/scrittura a Dato E
	 * 					 SharedR: l'elenco di utenti a cui è consentito l'accesso in sola lettura a Dato E
	 */
	
	// Restituisce il nome del proprietario del dato E
	public String getOwner();
	/*
	 * RETURN: Il campo <Owner> di this.
	 */
	
	// Restituisce il dato E 
	public E getData();
	/*
	 * RETURN: Il campo <Dato E> di this.
	 */
	
	// Restituisce un iteratore che genera tutti gli utenti che hanno accesso a this in sola lettura
	public Iterator<String> getIteratorRead();
	/*
	 * RETURN: Iteratore sull'elenco <SharedR> di this
	 */
	
	// Restituisce un iteratore che genera tutti gli utenti che hanno accesso a this in lettura/scrittura
	public Iterator<String> getIteratorWrite();
	/*
	 * RETURN: Iteratore sull'elenco <SharedW> di this
	 */
	
	// Segna il dato E come condiviso in scrittura/lettura con Other 
	public void setShareW(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * MODIFIES: this
	 * EFFECTS: Viene aggiunto Other all'elenco <SharedW> di this.
	 * 			Se Other era già presente in <SharedW> NON viene inserito nuovamente nell'elenco.
	 * 			Inoltre, se Other era presente in <SharedR>, viene rimosso da quest'ultimo.
	 */
	
	// Segna il dato E come condiviso in sola lettura con Other
	public void setShareR(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * MODIFIES: this
	 * EFFECTS: Viene aggiunto Other all'elenco <SharedR>, di this.
	 * 			Se Other era già presente in <SharedR> NON viene inserito nuovamente nell'elenco.
	 * 			Inoltre, se Other era presente in <SharedW>, viene rimosso da quest'ultimo.
	 */
	
	// Restituisce true se il dato E risulta condiviso in scrittura/lettura con Other, false altrimenti
	public boolean isSharedW(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * RETURN: true se Other è contenuto nell'elenco <SharedW> di this, false altrimenti
	 */
	
	// Restituisce true se il dato E risulta condiviso in sola lettura con Other, false altrimenti
	public boolean isSharedR(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * RETURN: true se Other è contenuto nell'elenco <SharedR> di this, false altrimenti
	 */

}