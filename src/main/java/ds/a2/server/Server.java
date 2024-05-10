package ds.a2.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import ds.a2.rmi.RMIServer;

public class Server{
	public static void main(String args[]) {
		try {
			RMIServer rs = new WhiteBoardServer();
			int port = Integer.parseInt(args[0]);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("whiteboardserver", rs);            
            System.out.println("the port: "+ port + " \nserver ready");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
}
