package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Cache extends Card {

    private static String name = "Cache";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.TREASURE);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Cache(int ID) {
        super(5, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.addMoney(3);
    }

    @Override
    public void onGain() {
        super.onGain();

        GameManagerObject.getCurrentTurn().gainCard(GameManagerObject.piles.get("Paladin.Model.CardTypes.Copper").drawCard());
        GameManagerObject.getCurrentTurn().gainCard(GameManagerObject.piles.get("Paladin.Model.CardTypes.Copper").drawCard());
    }
}
