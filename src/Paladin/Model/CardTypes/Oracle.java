package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Oracle extends Card {

    private static String name = "Oracle";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        cardTypes.add(CardType.ATTACK);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Oracle(int ID) {
        super(3, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);

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
                Card first = player.getDeck().drawCard();
                Card second = player.getDeck().drawCard();

                String wantToDiscard = Requester.askUserToSelectString(GameManagerObject.currentPlayer, options,
                        player.getName() + " revealed a(n) " + first.getName() + " and a(n) " + second.getName() + ".  Do you want them to discard them " +
                                "or put them back on top of their deck?", "Oracle");

                if (wantToDiscard.equals("Discard")) {
                    player.getDeck().discardCard(first);
                    player.getDeck().discardCard(second);
                } else {
                    player.getDeck().addCardToTopOfDrawPile(first, false);


                    ArrayList<Card> cardsToAdd = new ArrayList<>();

                    ArrayList<String> whichCard = new ArrayList<>();
                    whichCard.add(first.getName());
                    whichCard.add(second.getName());

                    String topCard = Requester.askUserToSelectString(player, whichCard,
                            "Which card do you want on top of your deck?", "Oracle");

                    if (topCard.equals(second.getName())) {
                        cardsToAdd.add(first);
                        cardsToAdd.add(second);
                    } else {
                        cardsToAdd.add(second);
                        cardsToAdd.add(first);
                    }

                    for (Card card : cardsToAdd) {
                        GameManagerObject.currentPlayer.getDeck().addCardToTopOfDrawPile(card, true);
                    }
                }
            }

        }



    }
}
