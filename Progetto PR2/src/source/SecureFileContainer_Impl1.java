package source;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

public class SecureFileContainer_Impl1<E> implements SecureFileContainer<E> {
	
	private HashMap<String, String> security;
	private HashMap<String, ArrayList<E>> data;
	
	public SecureFileContainer_Impl1() {
		security = new HashMap<String, String>(0);
		data = new HashMap<String, ArrayList<E>>(0);
	}


	public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyRegisteredException, WeakPasswordException {
		
		if (Id==null || passw==null)
			throw new NullPointerException();
		if (security.containsKey(Id))
			throw new UserAlreadyRegisteredException();
		if (passw.length() < 5)
			throw new WeakPasswordException();
		
	    security.put(Id, passw);
        data.put(Id, new ArrayList<>());	
	}


	public int getSize(String Owner, String passw) throws NullPointerException, UserNotFoundException, WrongPasswordException {
		
		if (Owner==null || passw==null)
			throw new NullPointerException();
		if (!security.containsKey(Owner))
			throw new UserNotFoundException();
		if (!security.get(Owner).equals(passw))
			throw new WrongPasswordException();
		
		return data.get(Owner).size();
		
	}

	@Override
	public boolean put(String Owner, String passw, E file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E get(String Owner, String passw, E file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E remove(String Owner, String passw, E file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void copy(String Owner, String passw, E file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shareR(String Owner, String passw, String Other, E file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shareW(String Owner, String passw, String Other, E file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<E> getIterator(String Owner, String passw)
			throws NullPointerException, UserNotFoundException, WrongPasswordException {
		// TODO Auto-generated method stub
		return null;
	}


}
