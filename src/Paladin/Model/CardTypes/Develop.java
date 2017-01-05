package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Develop extends Card {

    private static String name = "Develop";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Develop(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);


        ArrayList<Card> options = new ArrayList<>();

        for (Card card : turn.currentPlayer.getHand().getCards()) {
            options.add(card);
        }
        if (options.isEmpty()) {
            return;
        }

        Card selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options,
                "Choose card to remodel", "Remodel");

        turn.currentPlayer.getHand().removeCard(selected);
        GameManagerObject.trash.add(selected);



        options = new ArrayList<>();

        for (String s : GameManagerObject.piles.keySet()) {
            SupplyPile pile = GameManagerObject.piles.get(s);
            if (pile.getSize() != 0 && pile.getTopCard().getCost() <= selected.getCost() + 2) {
                options.add(pile.getTopCard());
            }
        }
        if (options.isEmpty()) {
            return;
        }

        selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options,
                "Choose card to gain, up to " + (selected.getCost() + 2), "Remodel");

        turn.gainCard(GameManagerObject.piles.get(selected.getClass().getName()).drawCard());

    }
}
