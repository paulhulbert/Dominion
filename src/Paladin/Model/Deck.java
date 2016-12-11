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

    private Player owner;

    public Deck(Player owner) {
        this.owner = owner;
    }

    /**
     * The last element in the draw pile is on top of the deck
     */
    private ArrayList<Card> drawPile = new ArrayList<>();


    private ArrayList<Card> discardPile = new ArrayList<>();


    public void addCardToDiscard(Card card, boolean wasGained) {
        discardPile.add(card);

        if (wasGained) {
            card.onGain();


            ArrayList<Player> players = GameManagerObject.getPlayersAsideFromSpecifiedInOrder(owner);
            players.add(0, owner);

            for (Player player : players) {
                for (Card cardInHand : player.getHand().getCards()) {
                    cardInHand.playerGainedCard(owner, card);
                }
            }
        }
    }

    public void discardCard(Card card) {
        addCardToDiscard(card, false);
        card.onDiscard();
    }

    public void addCardToTopOfDrawPile(Card card, boolean wasGained) {
        drawPile.add(card);

        if (wasGained) {
            card.onGain();


            ArrayList<Player> players = GameManagerObject.getPlayersAsideFromSpecifiedInOrder(owner);
            players.add(0, owner);

            for (Player player : players) {
                for (Card cardInHand : player.getHand().getCards()) {
                    cardInHand.playerGainedCard(owner, card);
                }
            }
        }
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
