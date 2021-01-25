package com.bruh;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame{
    //private static String[][] textFile = new String[10][3];
    private static ArrayList<String[]> textFile = new ArrayList<String[]>();
    //here, create a 2d array
    static{
        try{
            Scanner input = new Scanner(new File("D:\\partList.csv"));
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

    private Main(){
        //setting up variables required for program
        final double alDensity = 0.0968211426; //in pounds per cubic inch
        double materialThickness = 0.125;
        Material aluminum = new Material("aluminum", alDensity, 25);

        ArrayList<Piece> pieces = new ArrayList<Piece>();
        ArrayList<Piece> chassisPieces = new ArrayList<Piece>();
        ArrayList<Piece> premadePieces = new ArrayList<Piece>();
        setTitle("Ansh's Robotics Calculator");
        //setting up our window elements
        int resX = 500, resY = 500;
        setSize(resX, resY);
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
        JButton endButton = new JButton("View robot stats");
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
                for(int i = 0; i < chassisPieces.size(); i++){
                    pieces.add(chassisPieces.get(i));
                }
                for(int i = 0; i < premadePieces.size(); i++){
                    pieces.add(premadePieces.get(i));
                }
                for(int i = 0; i < pieces.size(); i++){
                    totalWeight += pieces.get(i).getWeight();
                    totalPrice += pieces.get(i).getPrice();
                }
                JOptionPane.showMessageDialog(endButton, "your robot's total weight is " + Math.round(totalWeight* 100.0)/100.0 + " pounds and costs $" + Math.round(totalPrice*100.0)/100.0);
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
                chassisPieces.add(new Piece(length, width, height, aluminum, materialThickness));
                data[0] = pieceToAdd.toString();
                data[1] = Math.round(pieceToAdd.getPrice() * 100.0)/100.0;
                data[2] = Math.round(pieceToAdd.getWeight()*100.0)/100.0;
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
                JComboBox pieceSelector = new JComboBox(menuItems);
                JButton addPiece = new JButton("add the selected piece to your robot build");
                addPiece.setBounds(100, 100, 100, 50);
                addPiece.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        int index = pieceSelector.getSelectedIndex();
                        premadePieces.add(new Piece(textFile.get(index)[0], Double.parseDouble(textFile.get(index)[1]), Double.parseDouble(textFile.get(index)[2])));
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
        mainPanel.add(customButton, BorderLayout.SOUTH);
        mainPanel.add(premadeButton, BorderLayout.SOUTH);
        mainPanel.add(endButton);
        table.setModel(piecesModel);
        tablePanel.add(headerLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.NORTH);

        headerLabel.setText("List of robot parts");

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(300, 300);
        table.setPreferredScrollableViewportSize(new Dimension(400, 150));
        table.setFillsViewportHeight(true);

        tablePanel.add(scrollPane, BorderLayout.NORTH);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
        new Main();
    }

}
