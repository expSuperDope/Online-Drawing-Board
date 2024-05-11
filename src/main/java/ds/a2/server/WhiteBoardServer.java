package ds.a2.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

import ds.a2.rmi.RMIClient;
import ds.a2.rmi.RMIServer;

public class WhiteBoardServer extends UnicastRemoteObject implements RMIServer{

    private ArrayList<User> users;
	private byte[] b;
    private ArrayList<String> chatHistory;

    public WhiteBoardServer() throws RemoteException {
        super();
		users = new ArrayList<User>();
        chatHistory = new ArrayList<String>();
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

                RMIClient wbc = (RMIClient) Naming.lookup("rmi://" + ip + ":" + port + "/" + serviceName);
                users.add(new User(userName, wbc));
                
                if(b != null)
                {
                    wbc.get(b);
                }

                ArrayList<String> usrNames = new ArrayList<>();
                for(User user : users){
                    usrNames.add(user.name);
                }

                for(User user : users){
                    try {
                        user.rmic.getUserList(usrNames);
                        user.rmic.getChatHistory(chatHistory);
                    } 
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }	
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
    public void removeUser(String name) throws RemoteException {
        
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.name.equals(name)) {
                iterator.remove();
                break;
            }
        }
        
        ArrayList<String> usrNames = new ArrayList<>();
        for(User user : users)
        {
            usrNames.add(user.name);
        }

        for(User user : users)
        {
            try {
                user.rmic.getUserList(usrNames);
            } 
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }	
    }

    @Override
    public void newChatMsg(String name, String msg) throws RemoteException {
        chatHistory.add(name + ": " + msg);
        for(User user : users){
            try {
                user.rmic.getChatHistory(chatHistory);
            } 
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }	
    }

    @Override
    public boolean ifDuplicate(String name) throws RemoteException {
        for(User u:users)
        {
            if(u.name.equals(name))
            {
                return true;
            }
        }
        return false;
    }
}
