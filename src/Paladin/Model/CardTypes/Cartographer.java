package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Cartographer extends Card {

    private static String name = "Cartographer";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Cartographer(int ID) {
        super(5, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.setCurrentActions(turn.getCurrentActions() + 1);
        turn.drawCard();


        ArrayList<Card> options = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            options.add(turn.currentPlayer.getDeck().drawCard());
        }
        if (options.isEmpty()) {
            return;
        }
        options.add(null);

        ArrayList<Card> selected = Requester.askUserToSelectManyCards(turn.currentPlayer, options,
                "Choose cards to discard", "Cartographer", 0, 9999);

        for (Card card : selected) {
            turn.currentPlayer.getDeck().discardCard(card);
        }


        while (!options.isEmpty()) {
            Card card = Requester.askUserToSelectSingleCard(turn.currentPlayer, options, "Which card do you want to put back next?", "Cartographer");

            turn.currentPlayer.getDeck().addCardToTopOfDrawPile(card, false);
            options.remove(card);
        }



    }
}
