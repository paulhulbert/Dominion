package Paladin.Model.CardTypes;

import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;
import com.google.gson.JsonElement;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Mine extends Card {

    private static String name = "Mine";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Mine(int ID) {
        super(1, name, ID);
    }

    @Override
    public void onPlay(Turn turn, JsonElement choices) throws GameLogicException {
        super.onPlay(turn, choices);


        ArrayList<Card> options = new ArrayList<>();

        for (Card card : turn.currentPlayer.getHand().getCards()) {
            if (Constants.cardTypes.get(card.getName()).contains(CardType.TREASURE)) {
                options.add(card);
            }
        }
        if (options.isEmpty()) {
            return;
        }

        Card selected = GameManagerObject.userRequester.askUserToSelectSingleCard(options,
                "Choose card to mine", "Mine");

        turn.currentPlayer.getHand().removeCard(selected);
        GameManagerObject.trash.add(selected);



        options = new ArrayList<>();

        for (String s : GameManagerObject.piles.keySet()) {
            SupplyPile pile = GameManagerObject.piles.get(s);
            if (pile.getSize() != 0 && pile.getTopCard().getCost() <= selected.getCost() + 3
                    && Constants.cardTypes.get(pile.getTopCard().getName()).contains(CardType.TREASURE)) {
                options.add(pile.getTopCard());
            }
        }
        if (options.isEmpty()) {
            return;
        }

        selected = GameManagerObject.userRequester.askUserToSelectSingleCard(options,
                "Choose treasure card to gain, up to " + (selected.getCost() + 3), "Mine");

        turn.getCardsGainedThisTurn().add(GameManagerObject.piles.get(selected.getClass().getName()).drawCard());

    }
}
