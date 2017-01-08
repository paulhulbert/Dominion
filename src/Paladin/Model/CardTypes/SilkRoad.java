package Paladin.Model.CardTypes;

import Paladin.Model.*;

import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class SilkRoad extends Card {

    private static String name = "SilkRoad";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.VICTORY);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public SilkRoad(int ID) {
        super(4, name, ID);
        howManyShouldPileContain = 8;
        if (GameManagerObject.players.size() > 2) {
            howManyShouldPileContain = 12;
        }
    }

    @Override
    public int getVictoryPointWorth(Player owner) {

        int total = 0;

        for (Card card : owner.getDeck().getDiscardPile()) {
            if (Constants.cardTypes.get(card.getName()).contains(CardType.VICTORY)) {
                total++;
            }
        }

        for (Card card : owner.getDeck().getDrawPile()) {
            if (Constants.cardTypes.get(card.getName()).contains(CardType.VICTORY)) {
                total++;
            }
        }

        for (Card card : owner.getHand().getCards()) {
            if (Constants.cardTypes.get(card.getName()).contains(CardType.VICTORY)) {
                total++;
            }
        }


        total /= 4;

        return super.getVictoryPointWorth(owner) + total;
    }
}
