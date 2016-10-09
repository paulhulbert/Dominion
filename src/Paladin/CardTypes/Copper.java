package Paladin.CardTypes;

import java.util.ArrayList;
import java.util.Arrays;

import Paladin.Card;
import Paladin.CardType;
import Paladin.Constants;
import Paladin.Turn;

/**
 * Created by paulh on 10/8/2016.
 */
public class Copper extends Card {



    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.TREASURE);
        Constants.cardTypes.put("Copper", cardTypes);
    }
    public Copper(int ID) {
        super(0, "Copper", ID);
    }

    @Override
    public void onPlay(Turn turn) {
        super.onPlay(turn);
        turn.addMoney(1);
    }
}
