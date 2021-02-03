package com.bruh;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame{
    private static ArrayList<String[]> textFile = new ArrayList<String[]>();
    //here, create a 2d array
    static{
        try{
            Scanner input = new Scanner(new File("partList.csv"));
            int i = 0;
            while(input.hasNextLine()){
                String[] temp = input.nextLine().split(",");
                textFile.add(temp);
            }
        }
        catch(Exception e){
            System.out.println("error " + e.getMessage() + "; cannot read file");
        }
    }

    private Main() throws IOException {
        //setting up files
        File workingFile = new File("savedRobots.txt");
        if(!workingFile.exists()) {
            workingFile.createNewFile();
        }
        RandomAccessFile savedBots = new RandomAccessFile("savedRobots.txt", "rw");
        //setting up variables required for program
        final double alDensity = 0.0968211426; //in pounds per cubic inch
        double materialThickness = 0.125;
        Material aluminum = new Material("aluminum", alDensity, 25);

        LinkedList<Piece> pieces = new LinkedList<Piece>();
        LinkedList<Piece> chassisPieces = new LinkedList<Piece>();
        LinkedList<Piece> premadePieces = new LinkedList<Piece>();

        //setting up our window elements
        setTitle("Ansh's Robotics Calculator");
        int resX = 500, resY = 500;
        setSize(resX, resY);
        setLocation(new Point(550 , 200));
        setLayout(new GridLayout(2, 1));
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.YELLOW);
        JPanel tablePanel = new JPanel();
        JLabel headerLabel = new JLabel("", JLabel.CENTER);
        JLabel statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(100,100);
        tablePanel.setLayout(new FlowLayout());
        tablePanel.setBackground(Color.YELLOW);

        //button setup
        JButton customButton = new JButton("Add a custom chassis piece");
        customButton.setBounds(150, 150, 100, 60);
        JButton premadeButton = new JButton("Add a pre-configured part from the list");
        premadeButton.setBounds(300, 300, 100, 60);
        JButton endButton = new JButton("View robot stats and end application");
        endButton.setBounds(500, 100, 100, 60);
        String[] columnNames = {"Part", "Price", "Weight"};
        Object[] data = new Object[3];
        JTable table = new JTable();
        DefaultTableModel piecesModel = new DefaultTableModel();
        piecesModel.setColumnIdentifiers(columnNames);

        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double totalWeight = 0;
                double totalPrice = 0;
                pieces.addAll(chassisPieces);
                pieces.addAll(premadePieces);
                for (Piece piece : pieces) {
                    totalWeight += piece.getWeight();
                    totalPrice += piece.getPrice();
                }
                JOptionPane.showMessageDialog(endButton, "your robot's total weight is " + Math.round(totalWeight* 100.0)/100.0 + " pounds and costs $" + Math.round(totalPrice*100.0)/100.0);
                String rowToAdd = "";
                try {
                    for (int i = 0; i < pieces.size(); i++) {
                        rowToAdd = (pieces.get(i).toString() + ", " + pieces.get(i).getPrice() + ", " + pieces.get(i).getWeight() + "\n");
                        savedBots.seek(savedBots.length());
                        savedBots.write(rowToAdd.getBytes());
                    }
                    savedBots.seek(savedBots.length());
                    savedBots.write(("end robot weight: " + totalWeight + ", price: " + totalPrice + ".").getBytes());
                }
                catch(Exception ioException){
                    System.out.println("failed to save robot data to file");
                }
                System.exit(0);
            }
        });

        customButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String dimensions = JOptionPane.showInputDialog("please enter the dimensions of your chassis piece in the form L W H. Ex: 5 8.7 10\n");
                Scanner keyboardInput = new Scanner(dimensions);
                double length = keyboardInput.nextDouble();
                double width = keyboardInput.nextDouble();
                double height = keyboardInput.nextDouble();
                Piece pieceToAdd = new Piece(length, width, height, aluminum, materialThickness);
                //chassisPieces.addTail(new Piece(length, width, height, aluminum, materialThickness));
                chassisPieces.add(pieceToAdd);
                data[0] = pieceToAdd.toString();
                data[1] = Math.round(pieceToAdd.getPrice() * 100.0)/100.0;
                data[2] = Math.round(pieceToAdd.getWeight() * 100.0)/100.0;
                piecesModel.addRow(data);
            }
        });

        premadeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String[] menuItems = new String[textFile.size()];
                for(int i = 0; i < textFile.size(); i++){
                    menuItems[i] = textFile.get(i)[0];
                }
                JFrame secondFrame = new JFrame("select a part!");
                secondFrame.setSize(400, 100);
                secondFrame.setLocation(getLocation().x+50, 100 + getLocation().y);
                JComboBox pieceSelector = new JComboBox(menuItems);
                JButton addPiece = new JButton("add the selected piece to your robot build");
                addPiece.setBounds(100, 100, 100, 50);
                addPiece.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        int index = pieceSelector.getSelectedIndex();
                        Piece pieceToAdd = new Piece(textFile.get(index)[0], Double.parseDouble(textFile.get(index)[1]), Double.parseDouble(textFile.get(index)[2]));
                        premadePieces.add(pieceToAdd);
                        data[0] = textFile.get(index)[0];
                        data[1] = Math.round(Double.parseDouble(textFile.get(index)[1]) * 100.0)/100.0;
                        data[2] = Math.round(Double.parseDouble(textFile.get(index)[2]) * 100.0)/100.0;
                        piecesModel.addRow(data);
                    }
                });
                JPanel newPan = new JPanel();
                newPan.add(pieceSelector, BorderLayout.NORTH);
                newPan.add(addPiece, BorderLayout.SOUTH);
                secondFrame.add(newPan);
                secondFrame.setVisible(true);
            }
        });

        //adding buttons to our two panels, then adding the two panels to our main screen
        mainPanel.add(customButton, BorderLayout.SOUTH);
        mainPanel.add(premadeButton, BorderLayout.SOUTH);
        mainPanel.add(endButton);
        table.setModel(piecesModel);
        tablePanel.add(headerLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.NORTH);

        headerLabel.setText("List of robot parts");

        //setting up table scrolling if number of items overflows
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(300, 300);
        table.setPreferredScrollableViewportSize(new Dimension(400, 150));
        table.setFillsViewportHeight(true);

        tablePanel.add(scrollPane, BorderLayout.NORTH);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) throws IOException {
        new Main(); // constructs a Main object that we added a constructor for
        //since we inherited the JFrame class in our Main class, it successfully creates a window
    }

}
