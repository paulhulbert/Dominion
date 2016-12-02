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


    public void addCardToDiscard(Card card) {
        discardPile.add(card);
    }

    public void discardCard(Card card) {
        addCardToDiscard(card);
        card.onDiscard();
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
        Collections.shuffle(drawPile, GameManagerObject.seededRandom);
    }




    public int getDrawSize() {
        return drawPile.size();
    }

    public int getDiscardSize() {
        return discardPile.size();
    }

    public ArrayList<Card> getDrawPile() {
        return drawPile;
    }

    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }
}
