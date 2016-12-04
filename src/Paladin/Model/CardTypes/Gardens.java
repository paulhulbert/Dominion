package Paladin.Model.CardTypes;

import Paladin.Model.*;

import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class Gardens extends Card {

    private static String name = "Gardens";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.VICTORY);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Gardens(int ID) {
        super(4, name, ID);
        howManyShouldPileContain = 8;
        if (GameManagerObject.players.size() > 2) {
            howManyShouldPileContain = 12;
        }
    }

    @Override
    public int getVictoryPointWorth(Player owner) {
        int total = owner.getDeck().getDiscardSize() + owner.getDeck().getDrawSize() + owner.getHand().getCards().size();

        total /= 10;

        return super.getVictoryPointWorth(owner) + total;
    }
}
