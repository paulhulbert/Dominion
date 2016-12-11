package Paladin.Model;

import Paladin.Controller.Requester;

import java.util.ArrayList;

/**
 * Created by paulh on 11/26/2016.
 */
public class FrequentUseCardMethods {

    public static Player whoOwnsThisCard(Card cardToLookFor) {

        for (Card card : GameManagerObject.getCurrentTurn().getCardsPlayedThisTurn()) {
            if (card.equals(cardToLookFor)) {
                return GameManagerObject.getCurrentTurn().currentPlayer;
            }
        }

        for (Card card : GameManagerObject.getCurrentTurn().getCardsGainedThisTurn()) {
            if (card.equals(cardToLookFor)) {
                return GameManagerObject.getCurrentTurn().currentPlayer;
            }
        }

        for (Player player : GameManagerObject.players) {
            for (Card card : player.getHand().getCards()) {
                if (card.equals(cardToLookFor)) {
                    return player;
                }
            }
            for (Card card : player.getDeck().getDiscardPile()) {
                if (card.equals(cardToLookFor)) {
                    return player;
                }
            }
            for (Card card : player.getDeck().getDrawPile()) {
                if (card.equals(cardToLookFor)) {
                    return player;
                }
            }
        }

        return null;
    }

    public static void forcePlayerToDiscardDownToX(int X, Player player) {

        int numberToDiscard = player.getHand().getCards().size() - X;

        if (numberToDiscard <= 0) {
            return;
        }

        ArrayList<Card> selected = Requester.askUserToSelectManyCards(player, player.getHand().getCards(),
                "Discard down to three cards, courtesy of " + GameManagerObject.currentPlayer.getName(), "Militia", numberToDiscard, numberToDiscard);

        for (Card card : selected) {
            player.getDeck().discardCard(card);
            player.getHand().removeCard(card);
        }


    }
}
