package Game;

import cards.ActionCards;
import cards.Cards;
import cards.NumberCard;
import cards.WildCard;
import exceptions.WrongCardPlayed;

import java.util.ArrayList;

public class Player {
    public String username;



    private ArrayList<Cards> cards_in_hand;
    public boolean declareUno;

    public Player(ArrayList<Cards> cards_in_hand) {
        this.cards_in_hand = cards_in_hand;
        this.declareUno = false;
    }
    public void drawCard(ArrayList deck) {
        ArrayList<Cards> card_deck = new ArrayList(deck);
        if (deck.size()!=0){
            cards_in_hand.add(card_deck.get(0));
            deck.remove(card_deck.get(0));
            System.out.println("Card drawn");

        }
        else{
            System.out.println("Deck is empty");

        }
    }
    public void declareUno(){
        if(cards_in_hand.size() == 1){
            declareUno = true;
            System.out.println("Uno declared");
        }
    }

    public void playCard(Cards card, GameSession gameSession,String color)  {
        System.out.println("Playing card: " + card.getClass());
        if(card instanceof NumberCard){
            playNumberCard((NumberCard) card,gameSession);
        }
        else if(card instanceof ActionCards){
            playActionCard((ActionCards) card,gameSession);
        }
        else{
            playWildCard(card,gameSession,color);
        }
    }
    public void playNumberCard(NumberCard card, GameSession gameSession) {
        GameValidation gameValidation = new GameValidation();
        if(gameValidation.validPlayNumberCard(card,gameSession)){
            gameSession.discardpile = card;
            cards_in_hand.remove(card);
            gameSession.color = card.color;
            System.out.println("Number card played "+card.number+" "+card.color);
        }
    }
    public void playActionCard(ActionCards card, GameSession gameSession)  {
        GameValidation gameValidation = new GameValidation();
        if(gameValidation.validPlayActionsCards(card,gameSession)){
            gameSession.discardpile = card;
            cards_in_hand.remove(card);
            if(card.skill=="reverse"){
                gameSession.clockwise = !gameSession.clockwise;
                gameSession.color= card.color;
                System.out.println("Game direction reversed");

            } else if (card.skill=="draw2") {
                Player next_player= gameSession.players.get(gameSession.findNextPlayer(gameSession.clockwise));
                for (int i = 0; i < 2; i++) {
                    next_player.drawCard(gameSession.deck);
                }
                gameSession.color= card.color;
                System.out.println("Next player drew 2 cards");

            } else if(card.skill=="skip"){
                gameSession.playerTurn = gameSession.findNextPlayer(gameSession.clockwise);
                gameSession.color= card.color;
                System.out.println("Next player skipped");
            }


        }
    }
    public void playWildCard(Cards card, GameSession gameSession, String color) {
        WildCard wildCard = (WildCard) card;
        if (wildCard.skill=="draw4") {
            System.out.println("Wild card played");
            Player next_player= gameSession.players.get(gameSession.findNextPlayer(gameSession.clockwise));
            System.out.println("Next player drew 4 cards");
            for (int i = 0; i < 4; i++) {
                next_player.drawCard(gameSession.deck);
            }
        }

        gameSession.color = color;
        gameSession.discardpile = card;
        cards_in_hand.remove(card);

    }





    public ArrayList<Cards> getCards_in_hand() {
        return cards_in_hand;
    }

    public void setCards_in_hand(ArrayList<Cards> cards_in_hand) {
        this.cards_in_hand = cards_in_hand;
    }


}
