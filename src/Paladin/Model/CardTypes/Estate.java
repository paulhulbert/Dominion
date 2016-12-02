package Paladin.Model.CardTypes;

import Paladin.Model.Card;
import Paladin.Model.CardType;
import Paladin.Model.Constants;
import Paladin.Model.Player;

import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class Estate extends Card {

    private static String name = "Estate";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.VICTORY);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Estate(int ID) {
        super(2, name, ID);
    }

    @Override
    public int getVictoryPointWorth(Player owner) {
        return super.getVictoryPointWorth(owner) + 1;
    }
}
