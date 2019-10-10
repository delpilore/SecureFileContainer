package source;

public interface File<E> {
	
	/*
	 * OVERVIEW: L'oggetto di tipo File rappresenta un tipo di dato astratto volto a contenere E e le informazioni di accesso proprie di E
	 * 
	 * TYPICAL ELEMENT: <E, Owner, SharedW, SharedR>
	 */
	
	public String getOwner();
	
	public E getData();
	
	public void setShareW(String Other);
	
	public void setShareR(String Other);
	
	public boolean isSharedW(String Other);
	
	public boolean isSharedR(String Other);
}
