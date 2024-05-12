package ds.a2.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import ds.a2.DrawType;
import ds.a2.EraserSize;
import ds.a2.rmi.RMIServer;

public class GUI extends JFrame implements ActionListener {

    public Board board;
    public String usrName;
    private JToolBar toolBar;
    private MenuBar mb;
    public RMIServer rmis;
    public boolean isManager;

    public ArrayList<String> usrList;
    public ArrayList<String> chatHistory;
    public JScrollPane usrArea;
    public JScrollPane chatPlace;
    public JTextField inputBox;
    JFileChooser fileChooser = new JFileChooser();
    public File boardFile; 
    public String savingType;

    public GUI(RMIServer rmis, String usrName, Boolean isManager) {
        super();
        this.rmis = rmis;
        this.usrName = usrName;
        this.isManager = isManager;
        initialize();
    }

    public void updateUserList()
    {
        JTextArea users = (JTextArea) usrArea.getViewport().getView();
        users.setText("");
        for(String u:usrList)
        {
            users.append(u + "\n");
        }
    }

    public void updateChatBox()
    {
        JTextArea chatBox = (JTextArea) chatPlace.getViewport().getView();
        chatBox.setText("");
        for(String c:chatHistory)
        {
            chatBox.append(c + "\n");
        }
    }

    public void initialize() {
        System.setProperty("user.language", "en");
        System.setProperty("user.country", "US");
        this.setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Online Drawing Board");

        //1. Menu Bar
        mb = new MenuBar();
        Menu file = new Menu("File");
        Menu manage = new Menu("Management");

        MenuItem newFile = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem close = new MenuItem("Close");
        MenuItem kick = new MenuItem("Kick");

        file.add(newFile);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(close);
        manage.add(kick);

        mb.add(file);
        mb.add(manage);
        if(isManager)
        {
            this.setMenuBar(mb);
        }
        
        newFile.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);
        close.addActionListener(this);
        kick.addActionListener(this);

        //2. Tool Bar
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
        toolBar.setPreferredSize(new Dimension(100,50));

        JButton button1 = new JButton("Line");
        JButton button2 = new JButton("Rectangle");
        JButton button3 = new JButton("Circle");
        JButton button4 = new JButton("Oval");
        JButton button5 = new JButton("Free Draw");
        JButton button7 = new JButton("Text");
        JButton colorButton = new JButton("Color");
        JButton eraser1 = new JButton("S Eraser");
        JButton eraser2 = new JButton("M Eraser");
        JButton eraser3 = new JButton("L Eraser");

        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        button1.setFont(buttonFont);
        button2.setFont(buttonFont);
        button3.setFont(buttonFont);
        button4.setFont(buttonFont);
        button5.setFont(buttonFont);
        button7.setFont(buttonFont);
        eraser1.setFont(buttonFont);
        eraser2.setFont(buttonFont);
        eraser3.setFont(buttonFont);
        colorButton.setFont(buttonFont);

        Dimension maxButtonSize = new Dimension(200, 50);
        button1.setMaximumSize(maxButtonSize);
        button2.setMaximumSize(maxButtonSize);
        button3.setMaximumSize(maxButtonSize);
        button4.setMaximumSize(maxButtonSize);
        button5.setMaximumSize(maxButtonSize);
        button7.setMaximumSize(maxButtonSize);
        colorButton.setMaximumSize(maxButtonSize);
        eraser1.setMaximumSize(maxButtonSize);
        eraser2.setMaximumSize(maxButtonSize);
        eraser3.setMaximumSize(maxButtonSize);

        toolBar.add(button1);
        toolBar.add(button2);
        toolBar.add(button3);
        toolBar.add(button4);
        toolBar.add(button5);
        toolBar.add(button7);
        toolBar.add(eraser1);
        toolBar.add(eraser2);
        toolBar.add(eraser3);
        toolBar.add(colorButton);

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
        button7.addActionListener(this);
        colorButton.addActionListener(this);
        eraser1.addActionListener(this);
        eraser2.addActionListener(this);
        eraser3.addActionListener(this);

        this.add(toolBar, BorderLayout.WEST);


        //3. Chat Room
        JPanel chatRoom = new JPanel(new BorderLayout());
        String showingName = "";
        if(isManager)
        {
            showingName = "User: " + usrName + "(Manager)";
        }
        else
        {
            showingName = "User: " + usrName;
        }
        JLabel name = new JLabel(showingName);
        chatRoom.add(name, BorderLayout.NORTH);

        JPanel listPanel = new JPanel(new BorderLayout());
        JTextArea userList = new JTextArea();
        userList.setEditable(false);
        usrArea = new JScrollPane(userList);
        usrArea.setPreferredSize(new Dimension(200, 120)); 
        listPanel.add(usrArea, BorderLayout.NORTH);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatPlace = new JScrollPane(chatArea);
        listPanel.add(chatPlace, BorderLayout.CENTER);
        chatRoom.add(listPanel,BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputBox = new JTextField();
        JButton send = new JButton("Send");
        inputPanel.add(inputBox, BorderLayout.CENTER);
        inputPanel.add(send, BorderLayout.EAST);
        chatRoom.add(inputPanel, BorderLayout.SOUTH);
        send.addActionListener(this);

        chatRoom.setPreferredSize(new Dimension(200, 150)); 
        this.add(chatRoom, BorderLayout.EAST);

        //4.
        board = new Board();
        board.setRMI(rmis);
        board.setBackground(Color.WHITE);
        this.add(board, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window is closing...");
                if(isManager)
                {
                    JOptionPane.showMessageDialog(null, "Please wait the application to be closed!", "Wait", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    rmis.removeUser(usrName);
                } catch (RemoteException e1) {
                    System.out.println("Server closed!");
                    System.exit(0);
                }
            }
        });

        fileChooser.setDialogTitle("Choose a file or directory");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setApproveButtonText("OK");

        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("JPG", "jpg");
        FileNameExtensionFilter textFilter = new FileNameExtensionFilter("PNG", "png");

        fileChooser.setFileFilter(imageFilter);
        fileChooser.setFileFilter(textFilter);
    }

    public void saveAs()
    {
        if(board != null)
        {
            BufferedImage image = board.getImageFromBoard();
            int result = fileChooser.showOpenDialog(this);

            FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();
            if (selectedFilter instanceof FileNameExtensionFilter) {
                FileNameExtensionFilter extensionFilter = (FileNameExtensionFilter) selectedFilter;
                String[] extensions = extensionFilter.getExtensions();
                if (extensions.length > 0) {
                    savingType = extensions[0];
                }
            }

            if (result == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                String fileName = "image"; 
                if (savingType.equals("jpg")) {
                    fileName += ".jpg";
                    BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = newImage.createGraphics();
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
                    g2d.drawImage(image, 0, 0, null);
                    g2d.dispose();
                    image = newImage;
                } else if (savingType.equals("png")) {
                    fileName += ".png";
                } else {
                    System.out.println("Unknown file type: " + savingType);
                    return;
                }
                boardFile = new File(path, fileName);
                System.out.println("Selected path: " + boardFile);
                try {
                    ImageIO.write(image, savingType, boardFile);
                    System.out.println(savingType);
                    System.out.println("Image saved successfully.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("User cancelled the operation");
            }
        }   
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("New")) {

            Board newBoard = new Board();
            newBoard.setRMI(rmis);
            newBoard.setBackground(Color.WHITE);
            getContentPane().remove(board);
            getContentPane().add(newBoard, BorderLayout.CENTER);
            board = newBoard;
            revalidate();
            repaint();
            boardFile = null;
            savingType = null;
            board.synchronize();
            try {
                rmis.setAllEnable(true);
            } catch (RemoteException e1) {
                System.out.println("Server closed!");
                System.exit(0);
            }

        } else if (command.equals("Open")) {

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                boardFile = fileChooser.getSelectedFile();
                String fileName = boardFile.getName();
                int dotIndex = fileName.lastIndexOf(".");
                if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
                    savingType = fileName.substring(dotIndex + 1);
                    System.out.println("File extension: " + savingType);
                } else {
                    System.out.println("No file extension found.");
                }

                try {
                    BufferedImage image = ImageIO.read(boardFile);
                    board.loadImage(image);
                    board.synchronize();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("User cancelled the operation");
            }

        } else if (command.equals("Save")) {
            if(board.getEnable() == false)
            {
                return;
            }

            if(boardFile == null)
            {
                saveAs();
            }
            else
            {
                BufferedImage image = board.getImageFromBoard();
                if (savingType.equals("jpg")) {
                    BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = newImage.createGraphics();
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
                    g2d.drawImage(image, 0, 0, null);
                    g2d.dispose();
                    image = newImage;
                }
                try {
                    ImageIO.write(image, savingType, boardFile);
                    System.out.println(savingType);
                    System.out.println("Image saved successfully.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } else if (command.equals("Save As")) {
            if(board.getEnable() == false)
            {
                return;
            }
            saveAs();
        } else if (command.equals("Close")) {

            Board newBoard = new Board();
            newBoard.setRMI(rmis);
            getContentPane().remove(board);
            getContentPane().add(newBoard, BorderLayout.CENTER);
            board = newBoard;
            revalidate();
            repaint();
            boardFile = null;
            savingType = null;
            board.synchronize();
            try {
                rmis.setAllEnable(false);
            } catch (RemoteException e1) {
                System.out.println("Server closed!");
                System.exit(0);
            }

        } else if (command.equals("Kick")) {
            String input = JOptionPane.showInputDialog(null, "Please enter the user name of user that you want to kick:");
            if (input != null) 
            {
                if(input.equals(this.usrName))
                {
                    JOptionPane.showMessageDialog(null, "You cannot kick yourself!", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    try {
                        rmis.kick(input);
                    } catch (RemoteException e1) {
                        
                    }
                }
            } 
        } else if (command.equals("Send")) {
            try {
                String msg = inputBox.getText();
                rmis.newChatMsg(usrName, msg);
            } catch (RemoteException e1) {
                System.out.println("Server closed!");
                System.exit(0);
            }
            inputBox.setText("");
        } 
        else if (command.equals("Line")) {
            board.changeMode(DrawType.Line);
        } else if (command.equals("Rectangle")) {
            board.changeMode(DrawType.Rectangle);
            System.out.println("2");
        } else if (command.equals("Circle")) {
            board.changeMode(DrawType.Circle);
            System.out.println("3");
        } else if (command.equals("Oval")) {
            board.changeMode(DrawType.Oval);
        } else if (command.equals("Free Draw")) {
            board.changeMode(DrawType.Free);
            System.out.println("5");
        } else if (command.equals("S Eraser")) {
            board.changeMode(DrawType.Eraser);
            board.changeSize(EraserSize.Small);
            System.out.println(board.currentSize);
            System.out.println("S");
        } else if (command.equals("M Eraser")) {
            board.changeMode(DrawType.Eraser);
            board.changeSize(EraserSize.Medium);
            System.out.println(board.currentSize);
            System.out.println("M");
        } else if (command.equals("L Eraser")) {
            board.changeMode(DrawType.Eraser);
            board.changeSize(EraserSize.Large);
            System.out.println(board.currentSize);
            System.out.println("L");
        } else if (command.equals("Text")) {
            board.changeMode(DrawType.Text);
            System.out.println("7");
        } else if (command.equals("Color")) {
            System.out.println("8");
            Color selectedColor = JColorChooser.showDialog(toolBar, "Choose a Color", Color.BLACK);
            if (selectedColor != null) 
            {
                board.setColor(selectedColor); 
            }
        } 
    }
}
