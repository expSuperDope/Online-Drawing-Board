package ds.a2.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServer extends Remote{
	public void synchronize(byte[] b) throws RemoteException;
    public void broadcast(String msg) throws RemoteException;
    public void registerInServer(String[] clientInf) throws RemoteException;
    public String weclome() throws RemoteException;
}
