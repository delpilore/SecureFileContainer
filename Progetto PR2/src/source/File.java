package source;

public interface File<E> {
	
	/*
	 * OVERVIEW: L'oggetto di tipo File<E> rappresenta un tipo di dato astratto volto a contenere E e le informazioni di accesso proprie di E
	 * 
	 * TYPICAL ELEMENT: <E, Owner, SharedW, SharedR> 
	 * 					 E: il dato E da incapsulare
	 * 					 Owner: il possessore del dato E
	 * 					 SharedW: l'elenco di utenti a cui è consentito l'accesso in lettura/scrittura al dato E
	 * 					 SharedR: l'elenco di utenti a cui è consentito l'accesso in lettura al dato E
	 */
	
	public String getOwner();
	
	public E getData();
	
	public void setShareW(String Other);
	
	public void setShareR(String Other);
	
	public boolean isSharedW(String Other);
	
	public boolean isSharedR(String Other);
}
