package ds.a2.Client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import ds.a2.rmi.RMIClient;
import ds.a2.rmi.RMIServer;

public class WhiteBoardClient extends UnicastRemoteObject implements RMIClient{

	public GUI gui;
	public boolean isManager;

	protected WhiteBoardClient(RMIServer rmis, String usrname, Boolean isManager) throws RemoteException {
		super();
		Locale.setDefault(new Locale("en", "US"));
		this.isManager = isManager;
		gui = new GUI(rmis, usrname, isManager);
		gui.setVisible(true);
	}

	@Override
	public void get(byte[] b) throws RemoteException {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(b);
			BufferedImage image = ImageIO.read(in);
			gui.board.loadImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void getUserList(ArrayList<String> names) throws RemoteException {
		gui.usrList = names;
		gui.updateUserList();
	}

	@Override
	public void getChatHistory(ArrayList<String> history) throws RemoteException {
		gui.chatHistory = history;
		gui.updateChatBox();
	}

	@Override
	public boolean checkNewUser(String name) throws RemoteException {
		int flag = JOptionPane.showConfirmDialog(null, name + " want to join the whiteboard\n" + "is it fine?","Check", JOptionPane.YES_NO_OPTION);
		if(flag == 1) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public void close() throws RemoteException {
        System.exit(0); 
	}

	@Override
	public void getServerMsg(String msg) throws RemoteException {
		JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
}
