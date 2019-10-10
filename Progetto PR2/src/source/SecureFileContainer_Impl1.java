package source;

import java.util.Iterator;

public class SecureFileContainer_Impl1 implements SecureFileContainer {

	@Override
	public void createUser(String Id, String passw)
			throws NullPointerException, UserAlreadyRegisteredException, WeakPasswordException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSize(String Owner, String passw)
			throws NullPointerException, UserNotFoundException, WrongPasswordException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean put(String Owner, String passw, Object file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object get(String Owner, String passw, Object file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object remove(String Owner, String passw, Object file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void copy(String Owner, String passw, Object file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shareR(String Owner, String passw, String Other, Object file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shareW(String Owner, String passw, String Other, Object file)
			throws NullPointerException, UserNotFoundException, WrongPasswordException, NoDataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator getIterator(String Owner, String passw)
			throws NullPointerException, UserNotFoundException, WrongPasswordException {
		// TODO Auto-generated method stub
		return null;
	}

}
