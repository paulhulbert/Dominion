package Test.Paladin.Model;

import Paladin.Model.*;
import Paladin.Model.CardTypes.Copper;
import Paladin.Model.Exceptions.GameLogicException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by paulh on 11/19/2016.
 */
public class DeckTest {


    private Thread setup() {
        try {
            GameManagerObject.uiInterface = new UnitTestUI();
            GameManagerObject.userRequester = new UnitTestUserRequester();
            GameManagerObject.setupGame(true);
            Thread starterThread = new Thread(new gameStarter());
            UnitTestUserRequester.waitLength = 10000;
            starterThread.start();
            Thread.sleep(100);
            return starterThread;
        } catch (GameLogicException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class gameStarter implements Runnable {
        @Override
        public void run() {
            try {
                GameManagerObject.startGame();
            } catch (GameLogicException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testDrawCard() {
        try {


            Thread starter = setup();

            int newCardID = Constants.getNewCardID();
            Card topCard = new Copper(newCardID);

            GameManagerObject.turns.get(0).currentPlayer.getDeck().addCardToTopOfDrawPile(topCard, false);


            Assert.assertEquals(topCard, GameManagerObject.turns.get(0).currentPlayer.getDeck().drawCard());

            GameManagerObject.gameOver = true;
            UnitTestUserRequester.waitLength = 1;
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShuffle() {
        try {

            setup();

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

}
