package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.Card;
import Paladin.Model.CardType;
import Paladin.Model.Constants;
import Paladin.Model.Exceptions.GameLogicException;
import Paladin.Model.Turn;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Chapel extends Card {

    private static String name = "Chapel";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Chapel(int ID) {
        super(2, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);


        ArrayList<Card> options = new ArrayList<>();

        for (Card card : turn.currentPlayer.getHand().getCards()) {
            options.add(card);
        }
        if (options.isEmpty()) {
            return;
        }
        options.add(null);

        ArrayList<Card> selected = Requester.askUserToSelectManyCards(turn.currentPlayer, options,
                "Choose cards to trash for chapel", "Chapel", 0, 9999);

        for (Card card : selected) {
            turn.trashCard(card);
        }


    }
}
