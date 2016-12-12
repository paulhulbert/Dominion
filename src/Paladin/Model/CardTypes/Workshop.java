package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Workshop extends Card {

    private static String name = "Workshop";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Workshop(int ID) {
        super(3, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);


        ArrayList<Card> options = new ArrayList<>();

        for (String s : GameManagerObject.piles.keySet()) {
            SupplyPile pile = GameManagerObject.piles.get(s);
            if (pile.getSize() != 0 && pile.getTopCard().getCost() <= 4) {
                options.add(pile.getTopCard());
            }
        }
        if (options.isEmpty()) {
            return;
        }

        Card selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options,
                "Choose card to gain", "Workshop");

        turn.gainCard(GameManagerObject.piles.get(selected.getClass().getName()).drawCard());

    }
}
