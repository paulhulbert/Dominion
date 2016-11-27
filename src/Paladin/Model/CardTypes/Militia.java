package Paladin.Model.CardTypes;

import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;
import com.google.gson.JsonElement;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Militia extends Card {
    private static String name = "Militia";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Militia(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onPlay(Turn turn, JsonElement choices) throws GameLogicException {
        super.onPlay(turn, choices);
        turn.addMoney(2);

        FrequentUseCardMethods.forceAllToDiscardDownToX(3);


    }
}
