package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Embassy extends Card {

    private static String name = "Embassy";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Embassy(int ID) {
        super(5, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.drawCard();
        turn.drawCard();
        turn.drawCard();
        turn.drawCard();
        turn.drawCard();

        ArrayList<Card> options = new ArrayList<>();

        for (Card card : turn.currentPlayer.getHand().getCards()) {
            options.add(card);
        }
        if (options.isEmpty()) {
            return;
        }

        ArrayList<Card> selected = Requester.askUserToSelectManyCards(turn.currentPlayer, options,
                "Choose a card to discard", "Oasis", 3, 3);

        for (Card card : selected) {
            turn.currentPlayer.getHand().getCards().remove(card);
            turn.currentPlayer.getDeck().discardCard(card);
        }
    }


    @Override
    public void onGain() {
        super.onGain();

        ArrayList<Player> players = GameManagerObject.getPlayersAsideFromSpecifiedInOrder(FrequentUseCardMethods.whoOwnsThisCard(this));

        for (Player player : players) {
            player.getDeck().addCardToDiscard(GameManagerObject.piles.get("Paladin.Model.CardTypes.Silver").drawCard(), true);
        }
    }
}
