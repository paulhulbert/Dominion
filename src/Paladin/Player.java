package Paladin;

import Paladin.CardTypes.Copper;
import Paladin.CardTypes.Estate;

/**
 * Created by paulh on 10/4/2016.
 */
public class Player {

    private String name = "Paul";

    private Deck deck = new Deck();
    private Hand hand = new Hand();


    public Player() {
        for (int i = 0; i < 7; i++) {
            deck.addCardToDiscard(new Copper(Constants.getNewCardID()));
        }
        for (int i = 0; i < 3; i++) {
            deck.addCardToDiscard(new Estate(Constants.getNewCardID()));
        }

        for (int i = 0; i < 5; i++) {
            hand.addCard(deck.drawCard());
        }
    }


    public static void main(String[] args) {
        Player player = new Player();
    }

    public String getName() {
        return name;
    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getHand() {
        return hand;
    }
}
