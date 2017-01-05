package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class FoolsGold extends Card {

    private static String name = "FoolsGold";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.TREASURE);
        cardTypes.add(CardType.REACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public FoolsGold(int ID) {
        super(2, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);


        boolean firstFoolsGold = true;

        for (Card card : turn.getCardsPlayedThisTurn()) {
            if (card.getName().equals(name)) {
                firstFoolsGold = false;
                break;
            }
        }

        if (firstFoolsGold) {
            turn.addMoney(1);
        } else {
            turn.addMoney(4);
        }
    }

    @Override
    public void playerGainedCard(Player player, Card card) {
        super.playerGainedCard(player, card);

        if (card.getName().equals("Province")) {
            Player ownerOfFoolsGold = FrequentUseCardMethods.whoOwnsThisCard(this);

            if (!player.equals(ownerOfFoolsGold)) {
                ArrayList<String> options = new ArrayList<>();
                options.add("Yes");
                options.add("No");

                String wantToTrash = Requester.askUserToSelectString(ownerOfFoolsGold, options,
                        "Do you want to trash your fool's gold and gain a gold?", "Fool's Gold");

                if (wantToTrash.equals("Yes")) {
                    GameManagerObject.trashCard(ownerOfFoolsGold, this);
                    ownerOfFoolsGold.getHand().removeCard(this);
                    ownerOfFoolsGold.getDeck().addCardToDiscard(GameManagerObject.piles.get(Gold.class.getName()).drawCard(), true);
                }
            }
        }
    }
}
