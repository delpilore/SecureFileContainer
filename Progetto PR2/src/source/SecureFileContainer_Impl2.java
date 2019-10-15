package source;

import java.util.Iterator;

public class SecureFileContainer_Impl2<E> implements SecureFileContainer<E> {

	@Override
	public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyRegisteredException,
			WeakPasswordException, IllegalUsernameException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSize(String Owner, String passw)
			throws NullPointerException, UserNotFoundException, WrongPasswordException {
		// TODO Auto-generated method stub
		return 0;
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
