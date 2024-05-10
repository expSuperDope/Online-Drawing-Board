package ds.a2.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClient extends Remote{	
	public void get(byte[] b) throws RemoteException;
	public void getMsgFromServer() throws RemoteException;
	public String weclome() throws RemoteException;
}
