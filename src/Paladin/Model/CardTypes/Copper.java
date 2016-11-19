package Paladin.Model.CardTypes;

import java.util.ArrayList;

import Paladin.Model.Card;
import Paladin.Model.CardType;
import Paladin.Model.Constants;
import Paladin.Model.Turn;
import com.google.gson.JsonElement;

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
    public void onPlay(Turn turn, JsonElement choices) {
        super.onPlay(turn, choices);
        turn.addMoney(1);
    }
}
