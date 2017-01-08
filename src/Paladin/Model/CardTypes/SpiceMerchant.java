package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.Card;
import Paladin.Model.CardType;
import Paladin.Model.Constants;
import Paladin.Model.Exceptions.GameLogicException;
import Paladin.Model.Turn;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class SpiceMerchant extends Card {

    private static String name = "SpiceMerchant";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public SpiceMerchant(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);



        ArrayList<Card> options = new ArrayList<>();

        for (Card card : turn.currentPlayer.getHand().getCards()) {
            if (Constants.cardTypes.get(card.getName()).contains(CardType.TREASURE)) {
                options.add(card);
            }
        }
        if (options.isEmpty()) {
            return;
        }

        options.add(null);

        Card selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options,
                "Choose card to trash", "Spice Merchant");

        if (selected != null) {
            turn.trashCard(selected);


            ArrayList<String> options2 = new ArrayList<>();
            options2.add("+2 cards, +1 action");
            options2.add("+2 money, +1 buy");

            String wantToDiscard = Requester.askUserToSelectString(turn.currentPlayer, options2,
                    "Choose an effect:", "Spice Merchant");

            if (wantToDiscard.equals("+2 cards, +1 action")) {
                turn.setCurrentActions(turn.getCurrentActions() + 1);
                turn.drawCard();
                turn.drawCard();
            } else {
                turn.addMoney(2);
                turn.setCurrentBuys(turn.getCurrentBuys() + 1);
            }
        }





    }
}
