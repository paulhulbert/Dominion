package Paladin.Model.CardTypes;

import Paladin.Model.*;

import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class Duchy extends Card {

    private static String name = "Duchy";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.VICTORY);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Duchy(int ID) {
        super(5, name, ID);
        howManyShouldPileContain = 8;
        if (GameManagerObject.players.size() > 2) {
            howManyShouldPileContain = 12;
        }
    }

    @Override
    public int getVictoryPointWorth(Player owner) {
        return super.getVictoryPointWorth(owner) + 3;
    }
}
