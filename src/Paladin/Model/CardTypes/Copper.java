package Paladin.Model.CardTypes;

import java.util.ArrayList;

import Paladin.Model.Card;
import Paladin.Model.CardType;
import Paladin.Model.Constants;
import Paladin.Model.Exceptions.GameLogicException;
import Paladin.Model.Turn;
import com.google.gson.JsonElement;

/**
 * Created by paulh on 10/8/2016.
 */
public class Copper extends Card {



    private static String name = "Copper";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.TREASURE);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Copper(int ID) {
        super(0, name, ID);
    }

    @Override
    public void onPlay(Turn turn, JsonElement choices) throws GameLogicException {
        super.onPlay(turn, choices);
        turn.addMoney(1);
    }
}
