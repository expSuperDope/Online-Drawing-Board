package ds.a2.Client;

import ds.a2.rmi.RMIClient;
import ds.a2.rmi.RMIServer;
import ds.a2.server.WhiteBoardServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.print.DocFlavor.STRING;

public class Client {

    public static int findAvailablePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            int port = socket.getLocalPort();
            socket.close(); 
            return port;
        }
    }

    public static void main(String args[]) {

		try {
            String serverIp = args[0];
            String serverPort = args[1];
            String userName = args[2];

            InetAddress localHost = InetAddress.getLocalHost();
            String clientIp = localHost.getHostAddress();
            int clientPort = findAvailablePort();
            System.out.println(clientIp);
            System.out.println(clientPort);
			RMIClient rc = new WhiteBoardClient();
            Registry registry = LocateRegistry.createRegistry(clientPort);
            registry.bind("whiteboardclient", rc);   
            System.out.println("the port: "+ clientPort + " \nserver ready");

            String[] cinf = new String[4];
            cinf[0] = userName;
            cinf[1] = clientIp;
            cinf[2] = String.valueOf(clientPort);
            cinf[3] = "whiteboardclient";
            RMIServer wbs = (RMIServer) Naming.lookup("rmi://" + serverIp + ":" + serverPort + "/whiteboardserver");
            wbs.registerInServer(cinf);  

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }    
	}
}
