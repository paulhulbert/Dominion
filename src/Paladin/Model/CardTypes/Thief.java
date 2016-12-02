package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Thief extends Card {
    private static String name = "Thief";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ATTACK);
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Thief(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);


        ArrayList<Card> trashedCards = new ArrayList<>();

        for (Player player : GameManagerObject.getPlayersAsideFromSpecifiedInOrder(GameManagerObject.currentPlayer)) {

            ArrayList<Card> options = new ArrayList<>();

            boolean blocked = false;
            for (Card card : player.getHand().getCards()) {
                if (card instanceof Moat) {
                    blocked = true;
                    break;
                }
            }

            Card card1 = player.getDeck().drawCard();
            Card card2 = player.getDeck().drawCard();

            if (card1 != null && Constants.cardTypes.get(card1.getName()).contains(CardType.TREASURE)) {
                options.add(card1);
            }
            if (card2 != null && Constants.cardTypes.get(card2.getName()).contains(CardType.TREASURE)) {
                options.add(card2);
            }

            if (!blocked) {

                if (!options.isEmpty()) {
                    Card selected = null;
                    if (options.size() == 2) {
                        selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options,
                                "Which card would you like to trash?  Owner:  " + GameManagerObject.currentPlayer.getName(),
                                "Thief");
                    } else if (options.size() == 1) {
                        selected = options.get(0);
                    }
                    trashedCards.add(selected);
                    player.getHand().removeCard(selected);
                }
            }

        }

        if (!trashedCards.isEmpty()) {
            trashedCards.add(null);
            ArrayList<Card> selected = Requester.askUserToSelectManyCards(turn.currentPlayer, trashedCards,
                    "Which treasures would you like to steal?",
                    "Thief", 0, 9999);

            for (Card card : trashedCards) {
                if (selected.contains(card)) {
                    turn.getCardsGainedThisTurn().add(card);
                } else {
                    GameManagerObject.trash.add(card);
                }
            }
        }


    }
}
