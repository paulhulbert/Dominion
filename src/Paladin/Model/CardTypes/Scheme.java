package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Scheme extends Card {

    private static String name = "Scheme";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Scheme(int ID) {
        super(3, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.setCurrentActions(turn.getCurrentActions() + 1);
        turn.drawCard();



    }

    @Override
    public void onCleanup(ArrayList<Card> cardsToRemove) throws GameLogicException {
        super.onCleanup(cardsToRemove);

        ArrayList<Card> options = new ArrayList<>();

        for (Card card : GameManagerObject.getCurrentTurn().getCardsPlayedThisTurn()) {
            if (card.equals(this)) {
                continue;
            }
            options.add(card);
        }
        if (options.isEmpty()) {
            return;
        }
        options.add(null);

        Card cardToRetain = Requester.askUserToSelectSingleCard(GameManagerObject.getCurrentTurn().currentPlayer, options,
                "Choose card to save", "Scheme");



        if (cardToRetain != null) {
            if (!GameManagerObject.getCurrentTurn().getCardsPlayedThisTurn().contains(cardToRetain)) {
                throw new GameLogicException("Scheme tried to keep a card that hadn't been played.");
            }
            GameManagerObject.getCurrentTurn().getCardsPlayedThisTurn().remove(cardToRetain);
            GameManagerObject.currentPlayer.getDeck().addCardToTopOfDrawPile(cardToRetain, false);
        }
    }
}
