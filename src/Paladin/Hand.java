package Paladin;

import java.util.ArrayList;

/**
 * Created by paulh on 10/4/2016.
 */
public class Hand {

    private ArrayList<Card> cards = new ArrayList<>();

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public void removeCard(int ID) {
        Card cardToRemove = null;
        for (Card card : cards) {
            if (card.getID() == ID) {
                cardToRemove = card;
                break;
            }
        }

        cards.remove(cardToRemove);
    }
}
