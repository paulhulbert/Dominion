package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class JackOfAllTrades extends Card {

    private static String name = "JackOfAllTrades";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public JackOfAllTrades(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);

        turn.gainCard(GameManagerObject.piles.get("Paladin.Model.CardTypes.Silver").drawCard());

        ArrayList<String> options = new ArrayList<>();
        options.add("Discard");
        options.add("Return");

        Player player = turn.currentPlayer;
        Card topCard = player.getDeck().drawCard();

        String wantToDiscard = Requester.askUserToSelectString(player, options,
                "You revealed a(n) " + topCard.getName() + ".  Do you want to discard it " +
                        "or put it back on top of your deck?", "Jack of all Trades");

        if (wantToDiscard.equals("Discard")) {
            player.getDeck().discardCard(topCard);
        } else {
            player.getDeck().addCardToTopOfDrawPile(topCard, false);
        }


        while (turn.currentPlayer.getHand().getCards().size() < 5) {
            Card nextCard = turn.currentPlayer.getDeck().drawCard();
            if (nextCard == null) {
                break;
            }

            turn.currentPlayer.getHand().addCard(nextCard);

        }


        ArrayList<Card> options2 = new ArrayList<>();

        for (Card card : turn.currentPlayer.getHand().getCards()) {
            if (!Constants.cardTypes.get(card.getName()).contains(CardType.TREASURE)) {
                options2.add(card);
            }
        }
        if (options2.isEmpty()) {
            return;
        }
        options2.add(null);

        Card selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options2,
                "Choose card to remodel", "Remodel");

        turn.trashCard(selected);



    }
}
