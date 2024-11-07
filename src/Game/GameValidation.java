package Game;

import cards.ActionCards;
import cards.Cards;
import cards.NumberCard;
import exceptions.WrongCardPlayed;

public class GameValidation {
    public boolean validPlayNumberCard(NumberCard card,GameSession gameSession)  {
        if (gameSession.discardpile instanceof NumberCard){
            if(card.color==gameSession.color || card.number==((NumberCard) gameSession.discardpile).number){
                return true;
            }
            else{
                return false;

            }

        }
        else{
            if(card.color==gameSession.color){
                return true;
            }
            else{
                return false;
            }
        }



    }
    public boolean validPlayActionsCards(ActionCards card, GameSession gameSession)  {
        if(card.color==gameSession.color ){
            return true;
        } else if (gameSession.discardpile instanceof ActionCards){
            if(card.skill.equals(((ActionCards) gameSession.discardpile).skill)){
                return true;
            }
            else{
                return false;
            }
        }

        else{
            return false;
        }

    }


}
