package Paladin;

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

    public void addCardToTopOfDrawPile(Card card) {
        drawPile.add(card);
    }

    public void addCardToBottomOfDrawPile(Card card) {
        drawPile.add(card);
    }

    public Card drawCard() {
        if (drawPile.isEmpty()) {
            Collections.shuffle(discardPile);
            drawPile = discardPile;
            discardPile = new ArrayList<>();
            //TODO: Fire off SQL message with new deck
        }

        if (drawPile.isEmpty()) {
            return null;
        }

        return drawPile.remove(drawPile.size() - 1);
    }


    public void shuffleDeck() {
        Collections.shuffle(drawPile);
    }
}
