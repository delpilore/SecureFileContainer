package source;

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
	 * RETURN: Il nome Owner relativo alla quadrupla <Dato E, Owner, SharedW, SharedR>
	 */
	
	// Restituisce il dato E 
	public E getData();
	/*
	 * RETURN: Il Dato E relativo alla quadrupla <Dato E, Owner, SharedW, SharedR>
	 */
	
	// Segna il dato E come condiviso in scrittura/lettura con Other 
	public boolean setShareW(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * MODIFIES: this
	 * EFFECTS: Si distinguono due casi: a) this era già stato condiviso ad Other / b) this è la prima volta che viene condiviso ad Other.
	 * 
	 * 			a) Il primo caso si riconosce da due possibili fattori: 1) Other è già presente in <SharedW> XOR 2) Other è già presente in <SharedR>.
	 * 				1) Non cambio nulla dello stato interno di this ed esco ritornando false.
	 * 				2) Tolgo Other da <SharedR>, lo aggiungo a <SharedW> ed esco ritornando false.
	 * 
	 * 			b) Il secondo caso si riconosce dal fatto che Other non è presente ne in <SharedW> ne in <SharedR> ed è quindi la prima volta 
	 * 			che viene condiviso ad Other. In questo caso aggiungo Other a <SharedW> e ritorno true
	 * 
	 * RETURN: true se this viene condiviso per la prima volta, false altrimenti
	 */
	
	// Segna il dato E come condiviso in sola lettura con Other
	public boolean setShareR(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * MODIFIES: this
	 * EFFECTS: Si distinguono due casi: a) this era già stato condiviso ad Other / b) this è la prima volta che viene condiviso ad Other.
	 * 
	 * 			a) Il primo caso si riconosce da due possibili fattori: 1) Other è già presente in <SharedR> XOR 2) Other è già presente in <SharedW>.
	 * 				1) Non cambio nulla dello stato interno di this ed esco ritornando false.
	 * 				2) Tolgo Other da <SharedW>, lo aggiungo a <SharedR> ed esco ritornando false.
	 * 
	 * 			b) Il secondo caso si riconosce dal fatto che Other non è presente ne in <SharedW> ne in <SharedR> ed è quindi la prima volta 
	 * 			che viene condiviso ad Other. In questo caso aggiungo Other a <SharedR> e ritorno true
	 * 
	 * RETURN: true se this viene condiviso per la prima volta, false altrimenti
	 */
	
	// Restituisce true se il dato E risulta condiviso in scrittura/lettura con Other, false altrimenti
	public boolean isSharedW(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * RETURN: true se Other è contenuto nell'elenco <SharedW> relativo alla quadrupla <Dato E, Owner, SharedW, SharedR>, false altrimenti
	 */
	
	// Restituisce true se il dato E risulta condiviso in sola lettura con Other, false altrimenti
	public boolean isSharedR(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * RETURN: true se Other è contenuto nell'elenco <SharedR> relativo alla quadrupla <Dato E, Owner, SharedW, SharedR>, false altrimenti
	 */

}
