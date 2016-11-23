package Paladin.Model.CardTypes;

import Paladin.Model.Card;
import Paladin.Model.CardType;
import Paladin.Model.Constants;

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
    }

    @Override
    public int getVictoryPointWorth() {
        return super.getVictoryPointWorth() - 1;
    }
}