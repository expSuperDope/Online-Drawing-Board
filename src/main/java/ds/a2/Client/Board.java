package ds.a2.Client;
import ds.a2.DrawType;
import ds.a2.EraserSize;
import ds.a2.rmi.RMIServer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel{

	private Point sPoint, ePoint;
    private DrawType currentMode = DrawType.Line;
	public EraserSize currentSize = EraserSize.Small;
	private Color currentColor = Color.BLACK;
	private RMIServer rmis;
	private BufferedImage board;
	private boolean enable = true;
	
    private static List<Point> points = new ArrayList<Point>();
    private static List<Shape> shapes = new ArrayList<Shape>();
	private static List<Shape> tmpShapes = new ArrayList<Shape>();
    
    public void setRMI(RMIServer rmis) {
    	this.rmis = rmis;
    }

	public boolean getEnable()
	{
		return enable;
	}

	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}

	public void changeMode(DrawType mode) {
		this.currentMode = mode;
	}

	public void changeSize(EraserSize size) {
		this.currentSize = size;
	}

	public void setColor(Color color){
		this.currentColor = color;
	}

    public void cleanAll() {
    	shapes.clear();
    	board = null;
    }

    public BufferedImage getImageFromBoard() {    	
    	int width = this.getWidth();
		int height = this.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);	
		Graphics2D paintToImage = image.createGraphics();
        this.paint(paintToImage);
        paintToImage.dispose();
        return image;
    }

    public void loadImage(BufferedImage image) {
    	cleanAll();
    	repaint();
    	this.board = image;
    }

    public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
        if(board != null) 
		{
        	g.drawImage(board, 0, 0, this);
        }
		for(Shape shape : shapes) {
			shape.generate_shape(g2d);
		}

		if(sPoint != null && ePoint != null)
		{
			g2d.setColor(currentColor);
			if(currentMode == DrawType.Free || currentMode == DrawType.Eraser)
			{
				for(Shape tmpShape : tmpShapes)
				{
					tmpShape.generate_shape(g2d);
				}

				if(currentMode == DrawType.Eraser)
				{
					g2d.setColor(Color.WHITE);
				}
				int s_x, s_y;
				if(points.size() == 0)
				{
					s_x = sPoint.x;
					s_y = sPoint.y;    
				}
				else
				{
					s_x = points.get(points.size() - 1).x;
					s_y = points.get(points.size() - 1).y;
				}
				g2d.drawLine(s_x, s_y, ePoint.x, ePoint.y);
				if(currentMode == DrawType.Free)
				{
					tmpShapes.add(new Shape(DrawType.Line, new Point(s_x, s_y), ePoint, currentColor));
				}
				else
				{
					tmpShapes.add(new Shape(DrawType.Line, new Point(s_x, s_y), ePoint, Color.white));
				}
				points.add(ePoint);
				g2d.setColor(currentColor);
			}

			int x = Math.min(sPoint.x, ePoint.x);
			int y = Math.min(sPoint.y, ePoint.y);
			int width = Math.abs(sPoint.x - ePoint.x);
			int height = Math.abs(sPoint.y - ePoint.y);

			if(currentMode == DrawType.Line) 
			{
				g2d.drawLine(sPoint.x, sPoint.y, ePoint.x, ePoint.y);
			}
			else if(currentMode == DrawType.Circle)
			{
				g2d.drawOval(x, y, Math.max(width, height), Math.max(width, height));
			}
			else if(currentMode == DrawType.Oval)
			{
				g2d.drawOval(x, y, width, height);
			}
			else if(currentMode == DrawType.Rectangle)
			{
				g2d.drawRect(x, y, width, height);
			}
		}
    }
	
	public void synchronize() {
		try {	        
			BufferedImage image = getImageFromBoard();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(image,"png", out);
			byte[] b = out.toByteArray();
			rmis.synchronize(b);
		} catch (RemoteException e) {
			System.out.println("Server closed!");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}

    public Board() {
        addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) 
			{
				if(!enable)
				{
					return;
				}
				sPoint = e.getPoint();
				ePoint = sPoint;
				points.add(sPoint);
				if(rmis != null && currentMode == DrawType.Text) 
				{
					Graphics2D g2d = (Graphics2D) getGraphics();
					String text;
					text = JOptionPane.showInputDialog("Please Enter:");
					if(text != null) {
						g2d.setColor(currentColor);
						g2d.drawString(text, sPoint.x, sPoint.y);
						shapes.add(new Shape(DrawType.Text, sPoint, text, currentColor));
						synchronize();
					}
				}
			}
		
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!enable)
				{
					return;
				}

				if(rmis != null) {
					ePoint = e.getPoint();
					Graphics2D g2d = (Graphics2D)getGraphics();
					g2d.setColor(currentColor);

					int x = Math.min(sPoint.x, ePoint.x);
            		int y = Math.min(sPoint.y, ePoint.y);
					int width = Math.abs(sPoint.x - ePoint.x);
					int height = Math.abs(sPoint.y - sPoint.y);
					
					if(currentMode == DrawType.Line) {
						g2d.drawLine(sPoint.x, sPoint.y, ePoint.x, ePoint.y);
						shapes.add(new Shape(DrawType.Line, sPoint, ePoint, currentColor));
					}
					else if(currentMode == DrawType.Circle)
					{
						g2d.drawOval(x, y, Math.max(width, height), Math.max(width, height));
						shapes.add(new Shape(DrawType.Circle, sPoint, ePoint, currentColor));
						sPoint = null;
						ePoint = null;
					}
					else if(currentMode == DrawType.Oval)
					{
						g2d.drawOval(x, y, width, height);
						shapes.add(new Shape(DrawType.Oval, sPoint, ePoint, currentColor));
					}
					else if(currentMode == DrawType.Rectangle)
					{
						g2d.drawRect(x, y, width, height);
						shapes.add(new Shape(DrawType.Rectangle, sPoint, ePoint, currentColor));
					}
					else if(currentMode == DrawType.Free)
					{
						List<Point> path = new ArrayList<Point>();
						path.addAll(points);
						shapes.add(new Shape(DrawType.Free, path, currentColor));
						tmpShapes.clear();
					}
					else if(currentMode == DrawType.Eraser)
					{
						List<Point> path = new ArrayList<Point>();
						path.addAll(points);
						shapes.add(new Shape(DrawType.Eraser, path, currentSize));
						tmpShapes.clear();
					}
					sPoint = null;
					ePoint = null;
					points.clear();
					repaint();
					synchronize();
				}	
			}
		});
		
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
				if(!enable)
				{
					return;
				}
				
            	if(rmis != null && sPoint != null) 
				{
					Graphics2D g2d = (Graphics2D)getGraphics();
					g2d.setColor(currentColor);
	                ePoint = e.getPoint();
					repaint();
            	}
            }
        });
	}
}
