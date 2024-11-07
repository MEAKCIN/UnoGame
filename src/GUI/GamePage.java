package GUI;

import Game.Bot;
import Game.GameSession;
import Game.Player;
import cards.ActionCards;
import cards.Cards;
import cards.NumberCard;
import cards.WildCard;
import exceptions.WrongCardPlayed;
import test.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePage extends JFrame {
    private JPanel mainPanel;
    private JLabel sessionNameLabel;
    private DefaultListModel<String> playerHandModel;
    private JList<String> playerHandList;
    private JButton playCardButton;
    private JButton drawCardButton;
    private JButton declareUnoButton;
    private JLabel[] botLabels;
    private JLabel discardPileLabel;
    private JLabel gameDirectionLabel;
    private JLabel colorDiscardPileLabel;
    private JLabel remainingDeckLabel;  // Label for remaining cards in deck
    private GameSession gameSession;
    private int playerTurn = 3; // Track player turn here

    public GamePage(GameSession gameSession) throws WrongCardPlayed {
        this.gameSession = gameSession;
        setTitle("Uno Game Page");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        setVisible(true);

        updateGameState();
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.DARK_GRAY);

        // Top Panel for session info and bots
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.DARK_GRAY);

        // Session Name
        sessionNameLabel = new JLabel("Session: " + gameSession.sessionID);
        sessionNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        sessionNameLabel.setForeground(Color.WHITE);
        topPanel.add(sessionNameLabel, BorderLayout.WEST);

        // Bots Information
        JPanel botsPanel = new JPanel(new GridLayout(1, 3));
        botsPanel.setBackground(Color.DARK_GRAY);
        botLabels = new JLabel[3];
        for (int i = 0; i < gameSession.players.size() - 1; i++) {
            Bot bot = (Bot) gameSession.players.get(i);
            botLabels[i] = new JLabel("Bot " + (i + 1) + ": " + bot.cardCount() + " cards", SwingConstants.CENTER);
            botLabels[i].setFont(new Font("Arial", Font.PLAIN, 18));
            botLabels[i].setForeground(Color.WHITE);
            botsPanel.add(botLabels[i]);
        }
        topPanel.add(botsPanel, BorderLayout.CENTER);

        // Remaining Deck
        remainingDeckLabel = new JLabel("Remaining Cards: " + gameSession.deck.size(), SwingConstants.RIGHT);
        remainingDeckLabel.setFont(new Font("Arial", Font.BOLD, 18));
        remainingDeckLabel.setForeground(Color.WHITE);
        topPanel.add(remainingDeckLabel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel for game state
        JPanel centerPanel = new JPanel(new GridLayout(2, 2));
        centerPanel.setBackground(Color.DARK_GRAY);

        // Discard Pile
        discardPileLabel = new JLabel("Discard Pile: " + getTopDiscardCard(), SwingConstants.CENTER);
        discardPileLabel.setFont(new Font("Arial", Font.BOLD, 18));
        discardPileLabel.setForeground(Color.WHITE);
        centerPanel.add(discardPileLabel);

        // Color of the discard pile
        colorDiscardPileLabel = new JLabel("Discard Pile Color: " + gameSession.color, SwingConstants.CENTER);
        colorDiscardPileLabel.setFont(new Font("Arial", Font.BOLD, 18));
        colorDiscardPileLabel.setForeground(Color.WHITE);
        centerPanel.add(colorDiscardPileLabel);

        // Game Direction
        gameDirectionLabel = new JLabel("Game Direction: " + (gameSession.clockwise ? "Clockwise" : "Counter-Clockwise"), SwingConstants.CENTER);
        gameDirectionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gameDirectionLabel.setForeground(Color.WHITE);
        centerPanel.add(gameDirectionLabel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Player Panel at the bottom
        playerHandModel = new DefaultListModel<>();
        playerHandList = new JList<>(playerHandModel);
        playerHandList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playerHandList.setBackground(Color.GRAY);
        playerHandList.setForeground(Color.BLACK);
        JScrollPane playerHandScrollPane = new JScrollPane(playerHandList);

        JLabel playerLabel = new JLabel("Cards in Hand:", SwingConstants.CENTER);
        playerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        playerLabel.setForeground(Color.WHITE);

        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBackground(Color.DARK_GRAY);
        playerPanel.add(playerLabel, BorderLayout.NORTH);
        playerPanel.add(playerHandScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.setBackground(Color.DARK_GRAY);

        drawCardButton = new JButton("Draw Card");
        drawCardButton.setBackground(Color.BLACK);
        drawCardButton.setForeground(Color.WHITE);
        drawCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    drawCard();
                } catch (WrongCardPlayed ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(drawCardButton);

        declareUnoButton = new JButton("Declare UNO");
        declareUnoButton.setBackground(Color.BLACK);
        declareUnoButton.setForeground(Color.WHITE);
        declareUnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                declareUno();
            }
        });
        buttonPanel.add(declareUnoButton);

        playCardButton = new JButton("Play Selected Card");
        playCardButton.setBackground(Color.BLACK);
        playCardButton.setForeground(Color.WHITE);
        playCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (playerTurn == 3) {
                        playSelectedCard();
                    }
                } catch (WrongCardPlayed ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(playCardButton);

        playerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(playerPanel, BorderLayout.SOUTH);

        // Adding mainPanel to Frame
        add(mainPanel);

        loadPlayerHand();
    }

    private String getTopDiscardCard() {
        if (gameSession.discardpile instanceof NumberCard) {
            NumberCard numberCard = (NumberCard) gameSession.discardpile;
            return numberCard.number + " " + numberCard.color;
        } else if (gameSession.discardpile instanceof ActionCards) {
            ActionCards actionCard = (ActionCards) gameSession.discardpile;
            return actionCard.skill + " " + actionCard.color;
        } else {
            WildCard wildCard = (WildCard) gameSession.discardpile;
            return wildCard.skill;
        }
    }

    private void loadPlayerHand() {
        Player player = (Player) gameSession.players.get(gameSession.players.size() - 1);
        playerHandModel.clear();

        for (Cards card : player.getCards_in_hand()) {
            if (card instanceof NumberCard) {
                NumberCard numberCard = (NumberCard) card;
                playerHandModel.addElement(numberCard.number + " " + numberCard.color);
            } else if (card instanceof ActionCards) {
                ActionCards actionCard = (ActionCards) card;
                playerHandModel.addElement(actionCard.skill + " " + actionCard.color);
            } else if (card instanceof WildCard) {
                WildCard wildCard = (WildCard) card;
                playerHandModel.addElement(wildCard.skill);
            }
        }
    }

    private void playSelectedCard() throws WrongCardPlayed {
        int selectedIndex = playerHandList.getSelectedIndex();
        Player player = (Player) gameSession.players.get(gameSession.players.size() - 1);
        if (selectedIndex != -1) {
            Cards selectedCard = player.getCards_in_hand().get(selectedIndex);
            System.out.println("Playing card: " + selectedCard);

            if (selectedCard instanceof WildCard) {
                String[] colors = {"Red", "Green", "Blue", "Yellow"};
                String selectedColor = (String) JOptionPane.showInputDialog(
                        this,
                        "Select a color:",
                        "Wild Card Color Selection",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        colors,
                        colors[0]
                );
                if (selectedColor != null) {
                    player.playCard(selectedCard, gameSession, selectedColor.toLowerCase());
                }
            } else {
                player.playCard(selectedCard, gameSession, "red"); // No color for non-wild cards
            }
            discardPileLabel.setText("Discard Pile: " + getTopDiscardCard()); // Update discard pile
            playerTurn = gameSession.findNextPlayer( gameSession.clockwise);
            updateGameState(); // Update state after playing card
        } else {
            JOptionPane.showMessageDialog(this, "Please select a card to play.");
        }
    }

    private void drawCard() throws WrongCardPlayed {
        Player player = (Player) gameSession.players.get(gameSession.players.size() - 1);
        player.drawCard(gameSession.deck);
        loadPlayerHand();
        JOptionPane.showMessageDialog(this, "You drew a card.");
        updateGameState();
    }

    private void declareUno() {
        Player player = (Player) gameSession.players.get(gameSession.players.size() - 1);
        player.declareUno();
        JOptionPane.showMessageDialog(this, "UNO declared!");
    }

    private void updateGameState() throws WrongCardPlayed {



        loadPlayerHand();
        botLabels[0].setText("Bot: " + gameSession.players.get(0) + " - Cards: " + ((Bot) gameSession.players.get(0)).cardCount());
        botLabels[1].setText("Bot: " + gameSession.players.get(1) + " - Cards: " + ((Bot) gameSession.players.get(1)).cardCount());
        botLabels[2].setText("Bot: " + gameSession.players.get(2) + " - Cards: " + ((Bot) gameSession.players.get(2)).cardCount());

        // Continue bot turns only after player has acted
        if (playerTurn != 3) {
            while (playerTurn != 3 && !gameSession.isGameEnd()) {
                System.out.println("Bot " + playerTurn + " turn");
                gameSession.playerTurnPlay( "red");
                discardPileLabel.setText("Discard Pile: " + getTopDiscardCard()); // Update discard pile after bot plays
                playerTurn = gameSession.findNextPlayer( gameSession.clockwise);
                gameSession.drawCards(gameSession.discardpile);
                remainingDeckLabel.setText("Remaining Cards: " + gameSession.deck.size());
                colorDiscardPileLabel.setText("Color of the discard pile: " + gameSession.color);
                gameDirectionLabel.setText("Game Direction: " + (gameSession.clockwise ? "Clockwise" : "Counter-Clockwise"));
                loadPlayerHand(); // Update player hand for UI
            }
             // Ensure final state is reflected in UI
        }

        if (gameSession.isGameEnd()) {
            playCardButton.setEnabled(false);
            drawCardButton.setEnabled(false);
            declareUnoButton.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Game Over!");
            dispose();
            MainPage mainPage = new MainPage(gameSession.username, gameSession.password);

        }
    }


}
