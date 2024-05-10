package ds.a2.Client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.imageio.ImageIO;
import ds.a2.rmi.RMIClient;

public class WhiteBoardClient extends UnicastRemoteObject implements RMIClient{

	private GUI gui;

	protected WhiteBoardClient() throws RemoteException {
		super();
		gui = new GUI();
	}

	@Override
	public void get(byte[] b) throws RemoteException {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(b);
			BufferedImage image = ImageIO.read(in);
			//gui.board.loadImage(image);
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
	public String weclome() throws RemoteException {
		return "Weclcome to client";
	}

}
