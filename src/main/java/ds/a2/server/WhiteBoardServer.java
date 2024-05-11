package ds.a2.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import ds.a2.rmi.RMIClient;
import ds.a2.rmi.RMIServer;

public class WhiteBoardServer extends UnicastRemoteObject implements RMIServer{

    private ArrayList<User> users;
	private byte[] b;

    public WhiteBoardServer() throws RemoteException {
        super();
		users = new ArrayList<User>();
    }

    @Override
	public void synchronize(byte[] b) throws RemoteException {
		this.b = b;
		for(User user : users){
			try {
				user.rmic.get(b);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}

    @Override
    public void broadcast(String msg) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'broadcast'");
    }

    @Override
    public void registerInServer(String[] clientInf) throws RemoteException {
        if(clientInf.length == 4)
        {
            try{
                String userName = clientInf[0];
                String ip = clientInf[1];
                String port = clientInf[2];
                String serviceName = clientInf[3];

                System.out.println(userName + " is coming!");
                RMIClient wbc = (RMIClient) Naming.lookup("rmi://" + ip + ":" + port + "/" + serviceName);
                System.out.println(wbc.weclome());
                users.add(new User(userName, wbc));

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }     
        }
    }

    @Override
    public String weclome() throws RemoteException {
        return "Welcome to Server"; 
    }
    
}
