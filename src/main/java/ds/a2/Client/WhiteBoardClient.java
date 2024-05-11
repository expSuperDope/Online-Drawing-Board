package ds.a2.Client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Locale;

import javax.imageio.ImageIO;
import ds.a2.rmi.RMIClient;
import ds.a2.rmi.RMIServer;

public class WhiteBoardClient extends UnicastRemoteObject implements RMIClient{

	public GUI gui;

	protected WhiteBoardClient(RMIServer rmis, String usrname) throws RemoteException {
		super();
		Locale.setDefault(new Locale("en", "US"));
		gui = new GUI(rmis, usrname);
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
	public void getMsgFromServer() throws RemoteException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getMsgFromServer'");
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
}
