package source;

// AUTHOR: Lorenzo Del Prete, Corso B, 531417

public interface File<E> {
	
	/*
	 * OVERVIEW: L'oggetto di tipo File<E> rappresenta un tipo di dato astratto volto a contenere E e le informazioni di accesso proprie di E
	 * 
	 * TYPICAL ELEMENT: <Dato E, Owner, SharedW, SharedR> 
	 * 					 Dato E: il dato E da incapsulare
	 * 					 Owner: il possessore del dato E
	 * 					 SharedW: l'elenco di utenti a cui è consentito l'accesso in lettura/scrittura al dato E
	 * 					 SharedR: l'elenco di utenti a cui è consentito l'accesso in sola lettura al dato E
	 */
	
	// Restituisce il nome del proprietario del dato E
	public String getOwner();
	/*
	 * RETURN: Il nome Owner relativo alla quadrupla <Dato E, Owner, SharedW, SharedR>
	 */
	
	// Restituisce il dato E 
	public E getData();
	/*
	 * RETURN: Il dato E relativo alla quadrupla <Dato E, Owner, SharedW, SharedR>
	 */
	
	// Segna il dato E come condiviso in scrittura/lettura con Other 
	public void setShareW(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: Viene aggiunto Other all'elenco <SharedW>, relativo alla quadrupla <Dato E, Owner, SharedW, SharedR>.
	 * 			Se Other era già presente in <SharedW> NON viene inserito nuovamente nell'elenco.
	 * 			Inoltre, se Other era presente in <SharedR>, viene rimosso da quest'ultimo.
	 */
	
	// Segna il dato E come condiviso in sola lettura con Other
	public void setShareR(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: Viene aggiunto Other all'elenco <SharedR>, relativo alla quadrupla <Dato E, Owner, SharedW, SharedR>.
	 * 			Se Other era già presente in <SharedR> NON viene inserito nuovamente nell'elenco.
	 * 			Inoltre, se Other era presente in <SharedW>, viene rimosso da quest'ultimo.
	 */
	
	// Restituisce true se il dato E risulta condiviso in scrittura/lettura con Other, false altrimenti
	public boolean isSharedW(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * RETURN: true se Other è contenuto nell'elenco <SharedW>, false altrimenti
	 */
	
	// Restituisce true se il dato E risulta condiviso in sola lettura con Other, false altrimenti
	public boolean isSharedR(String Other) throws NullPointerException;
	/*
	 * REQUIRES: Other!=null
	 * THROWS: Se Other==null -> NullPointerException (eccezione disponibile in Java, unchecked)
	 * RETURN: true se Other è contenuto nell'elenco <SharedR>, false altrimenti
	 */

}
