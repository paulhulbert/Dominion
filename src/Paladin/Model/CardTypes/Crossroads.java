package Paladin.Model.CardTypes;

import Paladin.Model.Card;
import Paladin.Model.CardType;
import Paladin.Model.Constants;
import Paladin.Model.Exceptions.GameLogicException;
import Paladin.Model.Turn;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Crossroads extends Card {

    private static String name = "Crossroads";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Crossroads(int ID) {
        super(2, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);

        int victoryCount = 0;

        for (Card card : turn.currentPlayer.getHand().getCards()) {
            if (Constants.cardTypes.get(card.getName()).contains(CardType.VICTORY)) {
                victoryCount++;
            }
        }

        boolean firstCrossroads = true;

        for (Card card : turn.getCardsPlayedThisTurn()) {
            if (card.getName().equals(name)) {
                firstCrossroads = false;
                break;
            }
        }

        for (int i = 0; i < victoryCount; i++) {
            turn.drawCard();
        }

        if (firstCrossroads) {
            turn.setCurrentActions(turn.getCurrentActions() + 3);
        }
    }
}
