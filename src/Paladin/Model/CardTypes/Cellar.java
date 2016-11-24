package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;
import com.google.gson.JsonElement;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Cellar extends Card {

    private static String name = "Cellar";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Cellar(int ID) {
        super(2, name, ID);
    }

    @Override
    public void onPlay(Turn turn, JsonElement choices) throws GameLogicException {
        super.onPlay(turn, choices);
        turn.setCurrentActions(turn.getCurrentActions() + 1);


        ArrayList<Card> options = new ArrayList<>();

        for (Card card : turn.currentPlayer.getHand().getCards()) {
            options.add(card);
        }
        if (options.isEmpty()) {
            return;
        }
        options.add(null);

        ArrayList<Card> selected = Requester.askUserToSelectManyCards(turn.currentPlayer, options,
                "Choose cards to discard for cellar", "Cellar", 0, 9999);

        for (Card card : selected) {
            turn.discardCard(card);
        }

        for (Card card : selected) {
            turn.drawCard();
        }

    }
}
