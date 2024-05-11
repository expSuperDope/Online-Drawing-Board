package ds.a2.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServer extends Remote{
	public void synchronize(byte[] b) throws RemoteException;
    public void registerInServer(String[] clientInf) throws RemoteException;
    public void removeUser(String userName) throws RemoteException;
    public void newChatMsg(String name, String msg) throws RemoteException;

    public boolean isFirst(String name) throws RemoteException;
    public boolean ifDuplicate(String name) throws RemoteException;
    public boolean isAllowed(String name) throws RemoteException;

    public void close() throws RemoteException;
}
