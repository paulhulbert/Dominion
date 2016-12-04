package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Moneylender extends Card {

    private static String name = "Moneylender";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Moneylender(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);



        for (Card card : turn.currentPlayer.getHand().getCards()) {
            if (card instanceof Copper) {
                turn.trashCard(card);
                turn.setCurrentMoney(turn.getCurrentMoney() + 3);
                break;
            }
        }





    }
}
