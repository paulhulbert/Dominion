package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.Card;
import Paladin.Model.CardType;
import Paladin.Model.Constants;
import Paladin.Model.Exceptions.GameLogicException;
import Paladin.Model.Turn;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class ThroneRoom extends Card {

    private static String name = "ThroneRoom";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public ThroneRoom(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);

        ArrayList<Card> actionCardsInHand = new ArrayList<>();
        for (Card card : turn.currentPlayer.getHand().getCards()) {
            if (Constants.cardTypes.get(card.getName()).contains(CardType.ACTION)) {
                actionCardsInHand.add(card);
            }
        }
        if (actionCardsInHand.isEmpty()) {
            return;
        }
        actionCardsInHand.add(null);
        Card cardToPlay = Requester.askUserToSelectSingleCard(turn.currentPlayer, actionCardsInHand,
                "Throne Room an action card",
                "Throne Room");
        if (cardToPlay == null) {
            return;
        }

        if (!turn.currentPlayer.getHand().contains(cardToPlay)) {
            throw new GameLogicException("Attempted to play card not in hand");
        }

        turn.getCardsPlayedThisTurn().add(cardToPlay);
        turn.currentPlayer.getHand().removeCard(cardToPlay);
        cardToPlay.onPlay(turn);
        turn.getCardsPlayedThisTurn().add(cardToPlay);
        cardToPlay.onPlay(turn);
        turn.getCardsPlayedThisTurn().remove(cardToPlay);
    }
}
