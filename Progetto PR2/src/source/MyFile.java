package source;

import java.util.ArrayList;
import java.util.Iterator;

// AUTHOR: Lorenzo Del Prete, Corso B, 531417

public class MyFile<E> implements File<E> {
	
    // AF(c) = { < c.Data, c.Owner, c.writePermissions, c.readPermissions > }
	
    // IR = (c.Owner != null) && (c.Data != null) && (c.readPermissions != null) && (c.writePermissions != null)
    //		&& (for all i,j. 0 <= i,j < c.readPermissions.size(), i!=j => readPermissions(i) != readPermissions(j))
	//		&& (for all i,j. 0 <= i,j < c.writePermissions.size(), i!=j => writePermissions(i) != writePermissions(j))
	//		&& (c.readPermissions.contains(User) => !c.writePermissions.contains(User))
	//		&& (c.writePermissions.contains(User) => !c.readPermissions.contains(User))
	
	private String Owner;								// Stringa che contiene il nome del proprietario del Dato E
	private E Data;										// Dato E
	private ArrayList<String> readPermissions;			// ArrayList che contiene i nomi degli utenti che hanno accesso in sola lettura al Dato E
	private ArrayList<String> writePermissions;			// ArrayList che contiene i nomi degli utenti che hanno accesso in scrittura/lettura al Dato E
	
	// Metodo costruttore
	public MyFile(String Id, E file) {
		Owner = Id;
		Data = file;
		
		readPermissions = new ArrayList<String>(0);
		writePermissions = new ArrayList<String>(0);
		
		// Owner viene inserito nell'elenco degli utenti che hanno accesso in lettura/scrittura
		writePermissions.add(Owner);
	}
	/*
	 * Il metodo costruttore preserva l'IR, perchè si limita ad inizializzare tutte le variabili d'istanza in modo che siano != null.
	 * Inoltre, aggiungendo Owner solo in writePermissions, rispetta l'ultima condizione dell'IR.
	 */

	// Restituisce il nome del proprietario del dato E
	public String getOwner() {
		return Owner;
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */
	
	// Restituisce il dato E 
	public E getData() {
		return Data;
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */
	
	// Restituisce un iteratore che genera tutti gli utenti che hanno accesso a this in sola lettura
	public Iterator<String> getIteratorRead() {
		Iterator<String> it = readPermissions.iterator();
		return it;
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */

	// Restituisce un iteratore che genera tutti gli utenti che hanno accesso a this in lettura/scrittura
	public Iterator<String> getIteratorWrite() {
		Iterator<String> it = writePermissions.iterator();
		return it;
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */

	// Segna il dato E come condiviso in scrittura/lettura con Other 
	public void setShareW(String Other) throws NullPointerException {
		
		if (Other==null)
			throw new NullPointerException();
		
		// Se Other era già nell'elenco writePermissions, non faccio nulla ed esco 
		if (this.isSharedW(Other))
			return;
		
		// Aggiungo Other all'elenco writePermissions
		writePermissions.add(Other);
		
		// Se Other faceva parte dell'elenco readPermissions, lo tolgo da esso 
		if (this.isSharedR(Other))
			readPermissions.remove(Other);
	}
	/*
	 * Questo metodo preserva l'IR perchè non ammette duplicati in writePermissions e inoltre non permette che 
	 * Other sia presente sia in writePermissions che in readPermissions, come da IR.
	 */

	// Segna il dato E come condiviso in sola lettura con Other
	public void setShareR(String Other) throws NullPointerException {
		
		if (Other==null)
			throw new NullPointerException();
		
		// Se Other era già nell'elenco readPermissions, non faccio nulla ed esco 
		if (this.isSharedR(Other))
			return;
		
		// Aggiungo Other all'elenco readPermissions
		readPermissions.add(Other);
		
		// Se Other faceva parte dell'elenco writePermissions, lo tolgo da esso 
		if (this.isSharedW(Other))
			writePermissions.remove(Other);
	}
	/*
	 * Questo metodo preserva l'IR perchè non ammette duplicati in readPermissions e inoltre non permette che 
	 * Other sia presente sia in readPermissions che in writePermissions, come da IR.
	 */

	// Restituisce true se il dato E risulta condiviso in scrittura/lettura con Other, false altrimenti
	public boolean isSharedW(String Other) throws NullPointerException {
		
		if (Other==null)
			throw new NullPointerException();
		
		if (writePermissions.contains(Other))
			return true;
		else
			return false;
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */

	// Restituisce true se il dato E risulta condiviso in sola lettura con Other, false altrimenti
	public boolean isSharedR(String Other) throws NullPointerException {
		
		if (Other==null)
			throw new NullPointerException();
		
		if (readPermissions.contains(Other))
			return true;
		else
			return false;
	}
	/*
	 * Questo metodo preserva banalmente l'IR visto che è semplicemente un'osservatore
	 */
} 