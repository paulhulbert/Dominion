package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Bureaucrat extends Card {
    private static String name = "Bureaucrat";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ATTACK);
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Bureaucrat(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.currentPlayer.getDeck().addCardToTopOfDrawPile(GameManagerObject.piles.get("Paladin.Model.CardTypes.Silver").drawCard());
        ArrayList<Player> players = GameManagerObject.getPlayersAsideFromSpecifiedInOrder(GameManagerObject.currentPlayer);

        for (Player player : players) {

            ArrayList<Card> options = new ArrayList<>();

            boolean blocked = false;
            for (Card card : player.getHand().getCards()) {
                if (card instanceof Moat) {
                    blocked = true;
                    break;
                }
                if (Constants.cardTypes.get(card.getName()).contains(CardType.VICTORY)) {
                    options.add(card);
                }
            }

            if (!blocked) {
                if (options.isEmpty()) {

                    JOptionPane.showMessageDialog(null, player.getName() + " has no victory cards in hand.  Hand: " +
                            player.getHand().toString());
                } else {
                    ArrayList<Card> selected = Requester.askUserToSelectManyCards(player, options,
                            "Place one victory card on top of your deck, courtesy of " + GameManagerObject.currentPlayer.getName(),
                            "Bureaucrat", 1, 1);

                    for (Card card : selected) {
                        player.getDeck().addCardToTopOfDrawPile(card);
                        player.getHand().removeCard(card);
                    }
                }
            }

        }


    }
}
