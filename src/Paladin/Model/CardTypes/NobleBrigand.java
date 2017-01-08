package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class NobleBrigand extends Card {
    private static String name = "NobleBrigand";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ATTACK);
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public NobleBrigand(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onBuy(Turn turn) throws GameLogicException {
        super.onBuy(turn);

        attackAction(turn);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.addMoney(1);

        attackAction(turn);


    }

    private void attackAction(Turn turn) throws GameLogicException {
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

            if (!blocked) {
                Card card1 = player.getDeck().drawCard();
                Card card2 = player.getDeck().drawCard();

                if (card1 != null && Constants.cardTypes.get(card1.getName()).contains(CardType.TREASURE)) {
                    options.add(card1);
                } else {
                    player.getDeck().addCardToDiscard(card1, false);
                }
                if (card2 != null && Constants.cardTypes.get(card2.getName()).contains(CardType.TREASURE)) {
                    options.add(card2);
                } else {
                    player.getDeck().addCardToDiscard(card2, false);
                }


                ArrayList<Card> actualOptions = new ArrayList<>();
                if (!options.isEmpty()) {

                    for (Card card : options) {
                        if (card.getName().equals("Silver") || card.getName().equals("Gold")) {
                            actualOptions.add(card);
                        }
                    }
                    if (!actualOptions.isEmpty()) {
                        Card selected = null;
                        if (actualOptions.size() == 2) {
                            selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, actualOptions,
                                    "Which card would you like to trash?  Owner:  " + GameManagerObject.currentPlayer.getName(),
                                    "Noble Brigand");
                        } else if (actualOptions.size() == 1) {
                            selected = actualOptions.get(0);
                        }
                        trashedCards.add(selected);
                        options.remove(selected);
                    }
                } else {
                    player.getDeck().addCardToDiscard(GameManagerObject.piles.get("Paladin.Model.CardTypes.Copper").drawCard(), true);
                }

                for (Card card : options) {
                    player.getDeck().addCardToDiscard(card, false);
                }
            }

        }

        if (!trashedCards.isEmpty()) {

            for (Card card : trashedCards) {
                turn.gainCard(card);

            }
        }
    }
}
