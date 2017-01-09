package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Trader extends Card {

    private static String name = "Trader";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        cardTypes.add(CardType.REACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Trader(int ID) {
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
                "Choose card to trash", "Trader");

        turn.trashCard(selected);


        for (int i = 0; i < selected.getCost(); i++) {
            turn.gainCard(GameManagerObject.piles.get("Paladin.Model.CardTypes.Silver").drawCard());
        }
    }

    @Override
    public void playerGainedCard(Player player, Card card) {
        super.playerGainedCard(player, card);

        Player owner = FrequentUseCardMethods.whoOwnsThisCard(this);

        if (player.equals(owner) && !(card instanceof Silver)) {
            ArrayList<String> options = new ArrayList<>();
            options.add("Yes");
            options.add("No");

            String wantToReplace = Requester.askUserToSelectString(player, options,
                    "You gained a " + card.getName() + ".  Do you want to gain a silver instead?", "Trader");

            if (wantToReplace.equals("Yes")) {

                if (GameManagerObject.getCurrentTurn().getCardsGainedThisTurn().contains(card)) {
                    GameManagerObject.getCurrentTurn().getCardsGainedThisTurn().remove(card);
                    GameManagerObject.getCurrentTurn().getCardsGainedThisTurn().add(GameManagerObject.piles.get("Paladin.Model.CardTypes.Silver").drawCard());
                }

                if (player.getDeck().getDrawPile().contains(card)) {
                    player.getDeck().getDrawPile().remove(card);
                    player.getDeck().addCardToTopOfDrawPile(GameManagerObject.piles.get("Paladin.Model.CardTypes.Silver").drawCard(), false);
                }

                if (player.getDeck().getDiscardPile().contains(card)) {
                    player.getDeck().getDiscardPile().remove(card);
                    player.getDeck().addCardToDiscard(GameManagerObject.piles.get("Paladin.Model.CardTypes.Silver").drawCard(), false);
                }

                if (player.getHand().getCards().contains(card)) {
                    player.getHand().getCards().remove(card);
                    player.getHand().addCard(GameManagerObject.piles.get("Paladin.Model.CardTypes.Silver").drawCard());
                }

            }
        }
    }
}
