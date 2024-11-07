package Game;

import cards.ActionCards;
import cards.Cards;
import cards.NumberCard;
import cards.WildCard;

import java.util.*;

public class Bot extends Player {

    public Bot(ArrayList<Cards> cards_in_hand) {
        super(cards_in_hand);
        boolean declearedUNO = false;
    }



    public void findCard(GameSession gameSession)  {
        GameValidation gameValidation = new GameValidation();
        boolean canplay = false;
        Cards card_selected = null;
        String color = "red";
        while(canplay==false) {
            for (Cards card : getCards_in_hand()) {
                if (canplay == false) {
                    if (card instanceof NumberCard) {
                        canplay = gameValidation.validPlayNumberCard((NumberCard) card, gameSession);
                        if (canplay) {
                            card_selected = card;

                            color = ((NumberCard) card).color;
                        }
                    } else if (card instanceof ActionCards) {
                        canplay = gameValidation.validPlayActionsCards((ActionCards) card, gameSession);
                        if (canplay) {
                            card_selected = card;

                            color = ((ActionCards) card).color;
                        }
                    } else if (card instanceof WildCard){
                        canplay = true;
                        HashMap<String, Integer> colorCount = new HashMap<>();
                        for (Cards c : getCards_in_hand()) {
                            if (c instanceof NumberCard) {
                                NumberCard numberCard = (NumberCard) c;
                                if (colorCount.containsKey(numberCard.color)) {
                                    colorCount.put(numberCard.color, colorCount.get(numberCard.color) + 1);
                                } else {
                                    colorCount.put(numberCard.color, 1);
                                }


                            } else if (c instanceof ActionCards) {
                                ActionCards actionCards = (ActionCards) c;
                                if (colorCount.containsKey(actionCards.color)) {
                                    colorCount.put(actionCards.color, colorCount.get(actionCards.color) + 1);
                                } else {
                                    colorCount.put(actionCards.color, 1);
                                }
                            }

                        }
                        color = Collections.max(colorCount.entrySet(), Map.Entry.comparingByValue()).getKey();
                        card_selected = card;

                    }
                } else {
                    continue;
                }
            }

            if (canplay == false) {
                drawCard(gameSession.deck);
                System.out.println("Bot drew a card");
            } else {
                System.out.println("Bot played card: " + card_selected.getClass());
                playCard(card_selected, gameSession, color);
            }
        }
        if(getCards_in_hand().size()==1){
            declareUno();
        }



    }



    public Integer cardCount(){
        return getCards_in_hand().size();
    }







}
