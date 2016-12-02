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
public class Library extends Card {

    private static String name = "Library";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Library(int ID) {
        super(5, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);

        ArrayList<Card> cardsSkipped = new ArrayList<>();

        ArrayList<String> options = new ArrayList<>();
        options.add("Yes");
        options.add("No");


        while (turn.currentPlayer.getHand().getCards().size() < 7) {
            Card nextCard = turn.currentPlayer.getDeck().drawCard();
            if (nextCard == null) {
                break;
            }
            if (Constants.cardTypes.get(nextCard.getName()).contains(CardType.ACTION)) {

                String wantToDiscard = Requester.askUserToSelectString(turn.currentPlayer, options,
                        "You drew a " + nextCard.getName() + ".  Do you want to skip it?", "Library");

                if (wantToDiscard.equals("Yes")) {
                    cardsSkipped.add(nextCard);
                } else {
                    turn.currentPlayer.getHand().addCard(nextCard);
                }
            } else {
                turn.currentPlayer.getHand().addCard(nextCard);
            }
        }

        for (Card card : cardsSkipped) {
            turn.currentPlayer.getDeck().discardCard(card);
        }


    }
}
