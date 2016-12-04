package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Spy extends Card {

    private static String name = "Spy";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        cardTypes.add(CardType.ATTACK);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Spy(int ID) {
        super(0, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.setCurrentActions(turn.getCurrentActions() + 1);
        turn.drawCard();

        ArrayList<String> options = new ArrayList<>();
        options.add("Discard");
        options.add("Return");

        ArrayList<Player> players = GameManagerObject.getPlayersAsideFromSpecifiedInOrder(GameManagerObject.currentPlayer);

        players.add(0, GameManagerObject.currentPlayer);

        for (Player player : players) {

            boolean blocked = false;
            for (Card card : player.getHand().getCards()) {
                if (card instanceof Moat && !player.equals(GameManagerObject.currentPlayer)) {
                    blocked = true;
                    break;
                }
            }

            if (!blocked) {
                Card topCard = player.getDeck().drawCard();

                String wantToDiscard = Requester.askUserToSelectString(GameManagerObject.currentPlayer, options,
                        player.getName() + " revealed a(n) " + topCard.getName() + ".  Do you want them to discard it " +
                                "or put it back on top of their deck?", "Spy");

                if (wantToDiscard.equals("Discard")) {
                    player.getDeck().discardCard(topCard);
                } else {
                    player.getDeck().addCardToTopOfDrawPile(topCard);
                }
            }

        }



    }
}
