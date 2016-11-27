package Paladin.Model;

import Paladin.Model.CardTypes.Copper;
import Paladin.Model.CardTypes.Estate;
import Paladin.Model.Exceptions.GameLogicException;

/**
 * Created by paulh on 10/4/2016.
 */
public class Player {

    private String name = "Paul";

    private Deck deck = new Deck();
    private Hand hand = new Hand();


    public Player(String name) throws GameLogicException {
        this.name = name;
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
        try {
            Player player = new Player("Paul");
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
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

    @Override
    public boolean equals(Object player) {
        return this.getName().equals(((Player) player).getName());
    }
}
