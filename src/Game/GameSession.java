package Game;

import Login.User;
import cards.ActionCards;
import cards.Cards;
import cards.NumberCard;
import cards.WildCard;
import exceptions.WrongCardPlayed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameSession {
    public String username;
    public String password;
    public int sessionID;
    public ArrayList<Cards> deck;
    public String color;

    public ArrayList<Player> players;
    public boolean clockwise;
    public Cards discardpile;
    public Integer playerTurn;


    public GameSession(String username,String password) {
        Random random = new Random();
        this.sessionID = random.nextInt(89999) + 10000;
        this.deck = setDeck();
        this.clockwise = true;
        this.discardpile = deck.get(50);
        this.username=username;
        this.password=password;
        this.players=createPlayer(this.deck);
        if (discardpile instanceof ActionCards){
            ActionCards card = (ActionCards) discardpile;
            this.color=card.color;
            }
        else if (discardpile instanceof NumberCard){
            NumberCard card = (NumberCard) discardpile;
            this.color=card.color;
        }
        else if (discardpile instanceof WildCard){
            this.color="red";
        }
        this.playerTurn=3;
    }


    public ArrayList<Cards> setDeck(){
        Cards cards = new Cards();
        cards.initializer();
        System.out.println("Game Session deck is created");
        return cards.deck;
    }
    public ArrayList<Player> createPlayer(ArrayList<Cards> deck){
        Cards cards= new Cards();
        cards.deck=deck;

        ArrayList<Player> players= new ArrayList();
        Bot bot1 = new Bot(cards.seperateCards(deck));
        Bot bot2 = new Bot(cards.seperateCards(deck));
        Bot bot3 = new Bot(cards.seperateCards(deck));
        Player player = new Player(cards.seperateCards(cards.deck));
        players.add(bot1);
        players.add(bot2);
        players.add(bot3);
        players.add(player);
        System.out.println("Count of players: "+players.size()+"\nPlayers are created"+"\n Name of the players: "+bot1.username+"\n"+bot2.username+"\n"+bot3.username+"\n"+player.username+"\n");
        return players;
    }
    public void drawCards(Cards discardpile){
            Player player = players.get(playerTurn);
            if (discardpile instanceof ActionCards) {
                ActionCards card = (ActionCards) discardpile;
                System.out.println("Action card is played");
                if (card.skill.equals("draw2")) {
                    if (deck.size()>2){
                        player.drawCard(deck);
                        player.drawCard(deck);
                        System.out.println("Player " + playerTurn + " draws 2 cards");
                    }
                    else{
                        System.out.println("Deck is empty");
                        deck.remove(0);
                        deck.remove(0);
                        isGameEnd();

                    }

                }
            }
            if (discardpile instanceof WildCard) {
                WildCard card = (WildCard) discardpile;
                if (card.skill.equals("draw4")) {
                    if (deck.size()>4){
                        player.drawCard(deck);
                        player.drawCard(deck);
                        player.drawCard(deck);
                        player.drawCard(deck);
                        System.out.println("Player " + playerTurn + " draws 4 cards");
                    }
                    else{
                        System.out.println("Deck is empty");
                        deck.remove(0);
                        deck.remove(0);
                        deck.remove(0);
                        deck.remove(0);
                        isGameEnd();
                    }

            }
        }
    }
    public void playerTurnPlay(String color) throws WrongCardPlayed {
        if (playerTurn != 3) {
            Bot bot = (Bot) players.get(playerTurn);
            System.out.println("Player " + playerTurn + " plays");
            bot.findCard(this);

            if (bot.cardCount() == 1) {
                bot.declareUno();
                System.out.println("Player " + playerTurn + " declares UNO");
            }
            if (bot.cardCount() == 0) {
                System.out.println("Player " + playerTurn + " wins");
            }
        }
        else if (playerTurn == 3){
            Player player = (Player) players.get(playerTurn);
            player.playCard(deck.get(0),this,color);
            System.out.println("Player " + playerTurn + " plays");
        }
    }



    public Integer findNextPlayer(boolean clockwise){
        if (clockwise){
            playerTurn++;
            ;
            if (playerTurn>3){
                playerTurn=0;
                System.out.println("Player turn is reset to 0");
            }
            System.out.println("Player turn is: "+playerTurn);
        }
        else{
            playerTurn--;
            if (playerTurn<0){
                playerTurn=3;
            }
            System.out.println("Player turn is: "+playerTurn);
        }
        return playerTurn;

    }

    public boolean isGameEnd() {
        Bot bot1 = (Bot) players.get(0);
        Bot bot2 = (Bot) players.get(1);
        Bot bot3 = (Bot) players.get(2);
        Player player = (Player) players.get(3);
        User user= new User(username,password);
        if (bot1.cardCount() == 0 || bot2.cardCount() == 0 || bot3.cardCount() == 0 ) {
            user.loseMatch(calculateScore(players));
            System.out.println("Bot wins");
            return true;
        } else if (player.getCards_in_hand().size()==0) {
            user.winMatch(calculateScore(players));
            System.out.println("Player wins");
            return true;
        } else if (deck.size()==0) {
            user.drawMatch(calculateScore(players));
            System.out.println("Draw");
            return true;
        } else {
            return false;
        }


    }
    public int calculateScore(ArrayList<Player> players){
        int score = 0;
        for (Player player:players){
            for(Cards card:player.getCards_in_hand()){
                if (card instanceof NumberCard){
                    score+=((NumberCard) card).number;
                }
                else if(card instanceof ActionCards){
                    score+=20;
                }
                else if (card instanceof WildCard){
                    score+=50;
                }

            }

        }
        System.out.println("Score is calculated: "+score);
        return score;

    }


}
