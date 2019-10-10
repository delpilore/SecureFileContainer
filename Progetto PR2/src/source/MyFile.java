package source;

import java.util.ArrayList;

public class MyFile<E> implements File<E> {
	
	private String Owner;
	private E Data;
	private ArrayList<String> readPermissions;
	private ArrayList<String> writePermissions;
	
	public MyFile(String Id, E file) {
		Owner = Id;
		Data = file;
		
		readPermissions = new ArrayList<String>(0);
		writePermissions = new ArrayList<String>(0);
		
		readPermissions.add(Owner);
		writePermissions.add(Owner);
	}

	public String getOwner() {
		return Owner;
	}

	public E getData() {
		return Data;
	}


	public void setShareW(String Other) {
		writePermissions.add(Other);
		
	}

	public void setShareR(String Other) {
		readPermissions.add(Other);
	}


	public boolean isSharedW(String Other) {
		if (writePermissions.contains(Other))
			return true;
		else
			return false;
	}

	public boolean isSharedR(String Other) {
		if (readPermissions.contains(Other))
			return true;
		else
			return false;
	}

}
