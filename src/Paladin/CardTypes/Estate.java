package Paladin.CardTypes;

import Paladin.Card;
import Paladin.CardType;
import Paladin.Constants;

import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class Estate extends Card {

    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.VICTORY);
        Constants.cardTypes.put("Estate", cardTypes);
    }
    public Estate(int ID) {
        super(0, "Estate", ID);
    }
}
