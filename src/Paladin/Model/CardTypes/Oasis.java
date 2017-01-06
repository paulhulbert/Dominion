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
public class Oasis extends Card {
    private static String name = "Oasis";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Oasis(int ID) {
        super(3, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.addMoney(1);
        turn.drawCard();
        turn.setCurrentActions(turn.getCurrentActions() + 1);

        ArrayList<Card> options = new ArrayList<>();

        for (Card card : turn.currentPlayer.getHand().getCards()) {
            options.add(card);
        }
        if (options.isEmpty()) {
            return;
        }

        Card selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options,
                "Choose a card to discard", "Oasis");

        turn.currentPlayer.getDeck().discardCard(selected);

    }
}
