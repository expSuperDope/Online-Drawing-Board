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
    private String managerName = null;

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
    public synchronized void registerInServer(String[] clientInf) throws RemoteException {
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


                chatHistory.add("[Server]: " + userName + " entered.");
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
    public synchronized void removeUser(String name) throws RemoteException {
        
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

        chatHistory.add("[Server]: " + name + " left.");
        if(name.equals(managerName))
        {
            chatHistory.add("[Server]: Manager left.\n[Server]: Application will close soon.");
            for(User user : users)
            {
                try {
                    user.rmic.getChatHistory(chatHistory);
                    user.rmic.close();
                } 
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }	
            System.exit(0);
        }
        
        for(User user : users)
        {
            try {
                user.rmic.getUserList(usrNames);
                user.rmic.getChatHistory(chatHistory);
            } 
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }	
    }

    @Override
    public void newChatMsg(String name, String msg) throws RemoteException {
        chatHistory.add("["+ name + "]: " + msg);
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

    @Override
    public boolean isAllowed(String name) throws RemoteException {
        for(User user : users){
            try {
                if(user.name.equals(managerName))
                {
                    return user.rmic.checkNewUser(name);
                }
            } 
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isFirst(String name) throws RemoteException {
        if(users.size() == 0)
        {
            this.managerName = name;
            return true;
        } 
        else
        {
            return false;
        }
    }

    @Override
    public void kick(String name) throws RemoteException {
        
        for(User user : users){
            if(user.name.equals(name))
            {
                chatHistory.add("[Server]: " + name + " was kicked.");
                user.rmic.getServerMsg("You are kicked by manager!");
                removeUser(name);
                user.rmic.close();
            }
        }
    }

    @Override
    public void setAllEnable(boolean enable) throws RemoteException {
        for(User u:users)
        {
            u.rmic.setEnable(enable);
        }
    }
}
