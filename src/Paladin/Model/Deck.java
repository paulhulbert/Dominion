package Paladin.Model;

import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 *
 *
 *
 *
 * Created by paulh on 10/4/2016.
 */
public class Deck {

    /**
     * The last element in the draw pile is on top of the deck
     */
    private ArrayList<Card> drawPile = new ArrayList<>();


    private ArrayList<Card> discardPile = new ArrayList<>();

    private ArrayList<Integer> shuffleSeed = new ArrayList<>();

    public void addCardToDiscard(Card card) {
        discardPile.add(card);
    }

    public void addCardToTopOfDrawPile(Card card) {
        drawPile.add(card);
    }

    public void addCardToBottomOfDrawPile(Card card) {
        drawPile.add(0,card);
    }

    public Card drawCard() throws GameLogicException {
        if (drawPile.isEmpty()) {
            drawPile = discardPile;
            discardPile = new ArrayList<>();
            shuffleDeck();

        }

        if (drawPile.isEmpty()) {
            return null;
        }

        return drawPile.remove(drawPile.size() - 1);
    }


    public void shuffleDeck() throws GameLogicException {
        if (shuffleSeed.isEmpty()) {
            Collections.shuffle(drawPile);
            //TODO: Fire off SQL message with new deck
        } else {
            ArrayList<Card> newDeck = new ArrayList<>();
            for (Integer i : shuffleSeed) {
                if (drawPile.contains(Constants.cards.get(i))) {
                    newDeck.add(Constants.cards.get(i));
                    drawPile.remove(Constants.cards.get(i));
                } else {
                    throw new GameLogicException("Tried to seed shuffle using a card not in the player's discard pile");
                }
            }

            if (!drawPile.isEmpty()) {
                throw new GameLogicException("Tried to seed shuffle but had more cards in draw than in seed");
            }

            drawPile = newDeck;

        }
    }


    public void setShuffleSeed(ArrayList<Integer> seed) {
        shuffleSeed = seed;
    }


    public int getDrawSize() {
        return drawPile.size();
    }

    public int getDiscardSize() {
        return discardPile.size();
    }
}
