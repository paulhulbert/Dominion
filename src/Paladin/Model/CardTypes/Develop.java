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
        super(3, name, ID);
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
                "Choose card to develop", "Develop");

        turn.currentPlayer.getHand().removeCard(selected);
        GameManagerObject.trash.add(selected);

        Card first = null;
        Card second = null;

        options = new ArrayList<>();

        for (String s : GameManagerObject.piles.keySet()) {
            SupplyPile pile = GameManagerObject.piles.get(s);
            if (pile.getSize() != 0 && pile.getTopCard().getCost() == selected.getCost() - 1) {
                options.add(pile.getTopCard());
            }
        }
        if (!options.isEmpty()) {
            selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options,
                    "Choose card to gain, costing exactly" + (selected.getCost() - 1), "Develop");

            first = GameManagerObject.piles.get(selected.getClass().getName()).drawCard();
        }

        for (String s : GameManagerObject.piles.keySet()) {
            SupplyPile pile = GameManagerObject.piles.get(s);
            if (pile.getSize() != 0 && pile.getTopCard().getCost() == selected.getCost() + 1) {
                options.add(pile.getTopCard());
            }
        }
        if (!options.isEmpty()) {
            selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options,
                    "Choose card to gain, costing exactly" + (selected.getCost() + 1), "Develop");


            second = GameManagerObject.piles.get(selected.getClass().getName()).drawCard();
        }

        ArrayList<Card> cardsToAdd = new ArrayList<>();

        if (first != null && second != null) {
            ArrayList<String> whichCard = new ArrayList<>();
            whichCard.add(first.getName());
            whichCard.add(second.getName());

            String topCard = Requester.askUserToSelectString(GameManagerObject.currentPlayer, whichCard,
                    "Which card do you want on top of your deck?", "Develop");

            if (topCard.equals(second.getName())) {
                cardsToAdd.add(first);
                cardsToAdd.add(second);
            } else {
                cardsToAdd.add(second);
                cardsToAdd.add(first);
            }
        } else if (first != null) {
            cardsToAdd.add(first);
        } else if (second != null) {
            cardsToAdd.add(second);
        }

        for (Card card : cardsToAdd) {
            GameManagerObject.currentPlayer.getDeck().addCardToTopOfDrawPile(card, true);
        }



    }
}
