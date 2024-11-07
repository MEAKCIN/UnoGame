package GUI;

import Game.GameSession;
import Login.User;
import exceptions.WrongCardPlayed;


import javax.swing.*;
import java.awt.*;

public class WelcomePage {
    private JFrame frame;
    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public WelcomePage() {
        initializeFrame();
        addComponents();
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("Uno Card Game Welcome");
        frame.setSize(1900, 1060);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel, BorderLayout.CENTER);
    }

    private void addComponents() {
        addLogo();
        addFields();
        addButtons();
    }

    private void addLogo() {
        ImageIcon unoLogo = new ImageIcon("C:/Users/Mehmet Emin/Desktop/comp132 project/uno game/src/data/UNO_Logo.svg.png");
        JLabel logoLabel = new JLabel(unoLogo);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel);
    }

    private void addFields() {
        panel.add(Box.createRigidArea(new Dimension(0, 20)));// Increased space between logo and fields
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(usernameLabel);
        usernameField = createTextField("Username");
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Increased space between fields
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(passwordLabel);
        passwordField = createPasswordField();
        panel.add(Box.createRigidArea(new Dimension(0, 20)));// Increased space between fields and buttons
    }

    private JTextField createTextField(String text) {
        JTextField field = new JTextField(20);
        field.setMaximumSize(field.getPreferredSize());
        field.setText(text);
        field.setForeground(Color.DARK_GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.DARK_GRAY);
                }
            }
        });
        panel.add(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setMaximumSize(field.getPreferredSize());
        field.setEchoChar((char) 0);
        field.setText("Password");
        field.setForeground(Color.GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).equals("Password")) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('*');
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).isEmpty()) {
                    field.setText("Password");
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0);
                }
            }
        });
        panel.add(field);
        return field;
    }

    private void addButtons() {
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(e -> processLogin());

        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        registerButton.addActionListener(e -> processRegistration());

        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Increased space between buttons
        panel.add(registerButton);
    }

    private void styleButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLUE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private void processLogin() {
        User user = new User(usernameField.getText(), new String(passwordField.getPassword()));
        user.login(usernameField.getText(), new String(passwordField.getPassword()));
        if (user.isLogged) {
            displayModernPopup("Successfully signed in!", "Login Success", new String[]{"Go to Main Page", "Play"});
        } else {
            JOptionPane.showMessageDialog(frame, "Login failed. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayModernPopup(String message, String title, String[] options) {
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Button.background", Color.BLUE);
        UIManager.put("Button.foreground", Color.WHITE);

        int response = JOptionPane.showOptionDialog(frame, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (response == JOptionPane.YES_OPTION) {
            frame.dispose();
            MainPage mainPage = new MainPage(usernameField.getText(),new String (passwordField.getPassword()));

        } else {
            try {
                GameSession gameSession = new GameSession(usernameField.getText(),new String(passwordField.getPassword()));
                GamePage gamePage = new GamePage(gameSession);
                frame.dispose();
            } catch (WrongCardPlayed e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processRegistration() {
        User user = new User(usernameField.getText(), new String(passwordField.getPassword()));
        if (user.register(usernameField.getText(), new String(passwordField.getPassword()))) {
            JOptionPane.showMessageDialog(frame, "User successfully created.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            new WelcomePage();
        } else {
            JOptionPane.showMessageDialog(frame, "Registration failed. User might already exist.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}