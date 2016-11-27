package Paladin.Model;

import Paladin.Controller.Requester;

import java.util.ArrayList;

/**
 * Created by paulh on 11/26/2016.
 */
public class FrequentUseCardMethods {

    public static void forceAllToDiscardDownToX(int X) {
        ArrayList<Player> players = GameManagerObject.getPlayersAsideFromSpecifiedInOrder(GameManagerObject.currentPlayer);

        for (Player player : players) {

            int numberToDiscard = player.getHand().getCards().size() - X;

            ArrayList<Card> selected = Requester.askUserToSelectManyCards(player, player.getHand().getCards(),
                    "Discard down to three cards, courtesy of " + GameManagerObject.currentPlayer.getName(), "Militia", numberToDiscard, numberToDiscard);

            for (Card card : selected) {
                player.getDeck().addCardToDiscard(card);
                player.getHand().removeCard(card);
            }

        }
    }
}
