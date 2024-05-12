package ds.a2.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIClient extends Remote{	
	public void get(byte[] b) throws RemoteException;
	public void getUserList(ArrayList<String> names) throws RemoteException;
	public void getChatHistory(ArrayList<String> history) throws RemoteException;
	public void getServerMsg(String msg) throws RemoteException;

	public boolean checkNewUser(String name) throws RemoteException;
	public void close() throws RemoteException;
	public void setEnable(boolean enable) throws RemoteException;
}
