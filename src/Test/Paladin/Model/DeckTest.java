package Test.Paladin.Model;

import Paladin.Model.*;
import Paladin.Model.CardTypes.Copper;
import Paladin.Model.Exceptions.GameLogicException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by paulh on 11/19/2016.
 */
public class DeckTest {



    @Test
    public void testDrawCard() {
        try {

            GameManagerObject.setupGame(true);

            int newCardID = Constants.getNewCardID();
            Card topCard = new Copper(newCardID);
            GameManagerObject.startGame();

            GameManagerObject.turns.get(0).currentPlayer.getDeck().addCardToTopOfDrawPile(topCard);


            Assert.assertEquals(topCard, GameManagerObject.turns.get(0).currentPlayer.getDeck().drawCard());

            GameManagerObject.turns.get(0).endTurn();
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShuffle() {
        try {

            GameManagerObject.setupGame(true);

            GameManagerObject.startGame();

            for (int i = 0; i < 3; i++) {
                GameManagerObject.turns.get(0).drawCard();
            }

            for (int i = 0; i < 2; i++) {
                GameManagerObject.turns.get(0).buyCard(Constants.cardIdentifiers.get("Copper"));
            }

            GameManagerObject.turns.get(0).endTurn();

            Assert.assertEquals(7, GameManagerObject.turns.get(0).currentPlayer.getDeck().getDrawSize());
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShuffleSeed() {
        try {

            GameManagerObject.setupGame(true);

            GameManagerObject.startGame();

            for (int i = 0; i < 3; i++) {
                GameManagerObject.turns.get(0).drawCard();
            }

            ArrayList<Integer> seed = new ArrayList<>();

            for (Card card : GameManagerObject.turns.get(0).currentPlayer.getHand().getCards()) {
                seed.add(card.getID());
            }

            Collections.shuffle(seed);

            GameManagerObject.turns.get(0).currentPlayer.getDeck().setShuffleSeed(seed);

            GameManagerObject.turns.get(0).endTurn();


            ArrayList<Integer> expected = new ArrayList<>();
            ArrayList<Integer> results = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                expected.add(seed.get(i));
                results.add(GameManagerObject.turns.get(0).currentPlayer.getDeck().drawCard().getID());
            }

            Collections.reverse(results);

            Assert.assertArrayEquals(expected.toArray(), results.toArray());

        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testShuffleSeedUsingMessage() {
        try {

            GameManagerObject.setupGame(true);

            GameManagerObject.startGame();

            for (int i = 0; i < 3; i++) {
                GameManagerObject.turns.get(0).drawCard();
            }

            ArrayList<Integer> seed = new ArrayList<>();

            for (Card card : GameManagerObject.turns.get(0).currentPlayer.getHand().getCards()) {
                seed.add(card.getID());
            }

            Collections.shuffle(seed);

            //GameManagerObject.turns.get(0).currentPlayer.getDeck().setShuffleSeed(seed);

            String seedArray = "[";
            for (Integer i : seed) {
                seedArray += i.intValue() + ",";
            }
            seedArray = seedArray.substring(0,seedArray.length()-1) + "]";

            GameManagerObject.addMessage(new Message(11,1,"Paul", "{ \"time_created\":1133442, \"GameID\":115, " +
                    "\"Player\":\"Paul\", \"Details\":{ \"type\":\"shuffle\", \"target\":\"" +
                    "Paul\", \"newDeck\":\"" + seedArray + "\" } }"));

            GameManagerObject.turns.get(0).endTurn();


            ArrayList<Integer> expected = new ArrayList<>();
            ArrayList<Integer> results = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                expected.add(seed.get(i));
                results.add(GameManagerObject.turns.get(0).currentPlayer.getDeck().drawCard().getID());
            }

            Collections.reverse(results);

            Assert.assertArrayEquals(expected.toArray(), results.toArray());

        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

}
