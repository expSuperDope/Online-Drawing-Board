package ds.a2.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import ds.a2.DrawType;
import ds.a2.EraserSize;
import ds.a2.rmi.RMIServer;

public class GUI extends JFrame implements ActionListener {

    public Board board;
    public String usrName;
    private JToolBar toolBar;
    public RMIServer rmis;

    public ArrayList<String> usrList;
    public ArrayList<String> chatHistory;
    public JScrollPane usrArea;
    public JScrollPane chatPlace;
    public JTextField inputBox;

    public GUI(RMIServer rmis, String usrName) {
        super();
        this.rmis = rmis;
        this.usrName = usrName;
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
        MenuBar mb = new MenuBar();
        Menu file = new Menu("File");
        Menu manage = new Menu("Management");

        MenuItem newFile = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem close = new MenuItem("Close");
        MenuItem kick = new MenuItem("Kick");
        MenuItem clearAll = new MenuItem("Clean All");

        file.add(newFile);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(close);
        manage.add(kick);
        manage.add(clearAll);

        mb.add(file);
        mb.add(manage);
        this.setMenuBar(mb);

        newFile.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);
        close.addActionListener(this);
        kick.addActionListener(this);
        clearAll.addActionListener(this);

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
        JButton button6 = new JButton("Eraser");
        JButton button7 = new JButton("Text");

        String[] options = {"Small", "Medium", "Large"};
        JComboBox<String> eraserSize = new JComboBox<>(options);

        JButton colorButton = new JButton("Color");
        colorButton.setFont(new Font("Arial", Font.PLAIN, 16));

        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        button1.setFont(buttonFont);
        button2.setFont(buttonFont);
        button3.setFont(buttonFont);
        button4.setFont(buttonFont);
        button5.setFont(buttonFont);
        button6.setFont(buttonFont);
        button7.setFont(buttonFont);
        eraserSize.setFont(buttonFont);

        Dimension maxButtonSize = new Dimension(150, 50);
        button1.setMaximumSize(maxButtonSize);
        button2.setMaximumSize(maxButtonSize);
        button3.setMaximumSize(maxButtonSize);
        button4.setMaximumSize(maxButtonSize);
        button5.setMaximumSize(maxButtonSize);
        button6.setMaximumSize(maxButtonSize);
        button7.setMaximumSize(maxButtonSize);
        eraserSize.setMaximumSize(maxButtonSize);
        eraserSize.setPreferredSize(maxButtonSize);

        toolBar.add(button1);
        toolBar.add(button2);
        toolBar.add(button3);
        toolBar.add(button4);
        toolBar.add(button5);
        toolBar.add(button7);
        toolBar.add(button6);
        toolBar.add(eraserSize);
        toolBar.add(colorButton);

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
        button6.addActionListener(this);
        button7.addActionListener(this);
        colorButton.addActionListener(this);

        eraserSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                String selectedOption = "";
                if (source instanceof JComboBox) 
                {
                    JComboBox<String> comboBox = (JComboBox<String>) source; // 安全地进行类型转换
                    selectedOption = (String) comboBox.getSelectedItem();
                } 
                else 
                {
                    System.out.println("Error!!");
                }

                if (selectedOption.equals("Small")) {
                    System.out.println("Option 1 selected");
                    board.changeSize(EraserSize.Small);
                } else if (selectedOption.equals("Medium")) {
                    System.out.println("Option 2 selected");
                    board.changeSize(EraserSize.Medium);
                } else if (selectedOption.equals("Large")) {
                    System.out.println("Option 3 selected");
                    board.changeSize(EraserSize.Large);
                }
            }
        });

        this.add(toolBar, BorderLayout.WEST);


        //3. Chat Room
        JPanel chatRoom = new JPanel(new BorderLayout());
        JLabel name = new JLabel("User: " + usrName);
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
                try {
                    rmis.removeUser(usrName);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("New")) {
            System.out.println("New clicked");
        } else if (command.equals("Open")) {
            System.out.println("Open clicked");
        } else if (command.equals("Save")) {
            System.out.println("Save clicked");
        } else if (command.equals("Save As")) {
            System.out.println("Save As clicked");
        } else if (command.equals("Close")) {
            System.out.println("Close clicked");
        } else if (command.equals("Kick")) {
            System.out.println("Kick clicked");
        } else if (command.equals("Clean All")) {
            System.out.println("Clean");
        } else if (command.equals("Send")) {
            try {
                String msg = inputBox.getText();
                rmis.newChatMsg(usrName, msg);
            } catch (RemoteException e1) {
                e1.printStackTrace();
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
        } else if (command.equals("Eraser")) {
            board.changeMode(DrawType.Eraser);
            System.out.println("6");
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
