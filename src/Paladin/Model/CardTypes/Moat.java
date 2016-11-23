package Paladin.Model.CardTypes;

import Paladin.Model.Card;
import Paladin.Model.CardType;
import Paladin.Model.Constants;
import Paladin.Model.Exceptions.GameLogicException;
import Paladin.Model.Turn;
import com.google.gson.JsonElement;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Moat extends Card{
    private static String name = "Moat";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        cardTypes.add(CardType.REACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Moat(int ID) {
        super(2, name, ID);
    }

    @Override
    public void onPlay(Turn turn, JsonElement choices) throws GameLogicException {
        super.onPlay(turn, choices);
        turn.drawCard();
        turn.drawCard();
    }
}
