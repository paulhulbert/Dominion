package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Chancellor extends Card {

    private static String name = "Chancellor";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Chancellor(int ID) {
        super(3, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.addMoney(2);

        ArrayList<String> options = new ArrayList<>();
        options.add("Yes");
        options.add("No");

        String wantToDiscard = Requester.askUserToSelectString(turn.currentPlayer, options,
                "Do you want to put your deck in your discard?", "Chancellor");

        if (wantToDiscard.equals("Yes")) {
            for (Card card : turn.currentPlayer.getDeck().getDrawPile()) {
                turn.currentPlayer.getDeck().getDiscardPile().add(card);

            }
            turn.currentPlayer.getDeck().getDrawPile().clear();
        }
    }
}
