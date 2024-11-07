package GUI;

import Game.GameSession;
import Login.User;
import Login.UserStatistics;
import exceptions.WrongCardPlayed;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainPage extends JFrame {
    private JLabel currentUserLabel;
    private JTable leaderboardTable;
    private JButton playButton;
    private JLabel imageLabel1;
    private JLabel imageLabel2;
    public String Password;

    public MainPage(String currentUser,String password) {
        setTitle("Uno Game - Main Page");
        setSize(1800, 920);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());

        currentUserLabel = new JLabel("Logged in as: " + currentUser, SwingConstants.CENTER);
        currentUserLabel.setForeground(Color.WHITE);
        add(currentUserLabel, BorderLayout.NORTH);

      //leaderBoard table;
        leaderBoard();



        // Add mouse listener to detect clicks on the "Details" button
        leaderboardTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = leaderboardTable.columnAtPoint(e.getPoint());
                int row = leaderboardTable.rowAtPoint(e.getPoint());
                if (column == 4) {  // Assuming the "Details" column is the 5th column
                    showDetails(row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        add(scrollPane, BorderLayout.CENTER);

        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 24));
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(Color.BLACK);
        playButton.addActionListener(e -> {
            try {
                startGame(currentUser,password);
            } catch (WrongCardPlayed ex) {
                throw new RuntimeException(ex);
            }
        });
        add(playButton, BorderLayout.SOUTH);

        try {
            imageLabel1 = new JLabel(new ImageIcon(ImageIO.read(new File("C:/Users/Mehmet Emin/Desktop/comp132 project/uno game/src/data/UNO_Logo.svg.png"))));
            imageLabel2 = new JLabel(new ImageIcon(ImageIO.read(new File("C:/Users/Mehmet Emin/Desktop/comp132 project/uno game/src/data/uno.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new FlowLayout());
        imagesPanel.add(imageLabel1);
        imagesPanel.add(imageLabel2);
        add(imagesPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private void showDetails(int row) {
        String username = (String) leaderboardTable.getValueAt(row, 0);
        JOptionPane.showMessageDialog(this, "Details for " + username);
        UserStatistics userStatistics = new UserStatistics();
        HashMap<String, ArrayList<Float>> allStats = userStatistics.getAllStatistics(username);
        // You can expand this method to show more detailed information
        JOptionPane.showMessageDialog(this, "Win Count: " + allStats.get(username).get(0) + "\n" +
                "Lose Count: " + allStats.get(username).get(1) + "\n" +
                "Total Game Count: " + allStats.get(username).get(2) + "\n" +
                "Score: " + allStats.get(username).get(3) + "\n" +
                "Average Score Per Game: " + allStats.get(username).get(4) + "\n" +
                "Win Loss Ratio: " + allStats.get(username).get(5) + "\n");

    }

    private void startGame(String currentUser,String password) throws WrongCardPlayed {
        System.out.println("Starting game...");
        dispose();
        GameSession gameSession = new GameSession(currentUser,password);
        GamePage gamePage = new GamePage(gameSession);


    }
    public void leaderBoard(){UserStatistics userStatistics = new UserStatistics();
        HashMap<String, ArrayList<Float>> allStats = userStatistics.getSimpleStatistics();

        // Columns for the JTable
        String[] columnNames = {"Username", "Win Count", "Total Game Played", "Score", "Details"};

        // Create the data array
        Object[][] data = new Object[allStats.size()][5];
        int index = 0;
        for (String username : allStats.keySet()) {
            ArrayList<Float> stats = allStats.get(username);
            data[index][0] = username;
            data[index][1] = stats.get(0);
            data[index][2] = stats.get(2);
            data[index][3] = stats.get(3);
            data[index][4] = "Details";
            index++;
        }

        // Create table model and JTable
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);





        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        leaderboardTable = new JTable(model);
        leaderboardTable.setPreferredScrollableViewportSize(new Dimension(1800, 800));
        leaderboardTable.setFillsViewportHeight(true);
        leaderboardTable.setFont(new Font("Serif", Font.BOLD, 24));
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setBackground(Color.GRAY);
        leaderboardTable.setForeground(Color.WHITE);
    }


}
