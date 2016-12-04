package Paladin.Model.CardTypes;

import Paladin.Model.*;

import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class Curse extends Card {

    private static String name = "Curse";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.VICTORY);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Curse(int ID) {
        super(0, name, ID);

        howManyShouldPileContain = 10 * (GameManagerObject.players.size() - 1);

        if (howManyShouldPileContain < 10) {
            howManyShouldPileContain = 10;
        }
    }

    @Override
    public int getVictoryPointWorth(Player owner) {
        return super.getVictoryPointWorth(owner) - 1;
    }
}
