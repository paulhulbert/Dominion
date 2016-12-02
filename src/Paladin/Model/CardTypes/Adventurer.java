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
public class Adventurer extends Card {

    private static String name = "Adventurer";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Adventurer(int ID) {
        super(6, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);

        ArrayList<Card> cardsSkipped = new ArrayList<>();

        int totalTreasures = 0;

        while (totalTreasures < 2) {
            Card nextCard = turn.currentPlayer.getDeck().drawCard();
            if (nextCard == null) {
                break;
            }
            if (Constants.cardTypes.get(nextCard.getName()).contains(CardType.TREASURE)) {
                turn.currentPlayer.getHand().addCard(nextCard);
                totalTreasures++;
            } else {
                cardsSkipped.add(nextCard);
            }
        }

        for (Card card : cardsSkipped) {
            turn.currentPlayer.getDeck().discardCard(card);
        }


    }
}
