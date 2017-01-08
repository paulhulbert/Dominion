package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Duchess extends Card {

    private static String name = "Duchess";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Duchess(int ID) {
        super(2, name, ID);
    }


    /**
     * Logic for the gaining this when duchy is purchased is in the duchy card
     *
     * @param turn
     * @throws GameLogicException
     */
    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);

        turn.addMoney(2);

        ArrayList<String> options = new ArrayList<>();
        options.add("Discard");
        options.add("Return");

        ArrayList<Player> players = GameManagerObject.getPlayersAsideFromSpecifiedInOrder(GameManagerObject.currentPlayer);

        players.add(0, GameManagerObject.currentPlayer);

        for (Player player : players) {


            Card topCard = player.getDeck().drawCard();

            String wantToDiscard = Requester.askUserToSelectString(player, options,
                    "You revealed a(n) " + topCard.getName() + " due to a duchess.  Do you want to discard it " +
                            "or put it back on top of your deck?", "Duchess");

            if (wantToDiscard.equals("Discard")) {
                player.getDeck().discardCard(topCard);
            } else {
                player.getDeck().addCardToTopOfDrawPile(topCard, false);
            }


        }



    }
}
