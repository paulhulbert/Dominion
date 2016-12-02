package Paladin.Model.CardTypes;

import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Witch extends Card {

    private static String name = "Witch";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ATTACK);
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Witch(int ID) {
        super(5, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.drawCard();
        turn.drawCard();

        for (Player player : GameManagerObject.getPlayersAsideFromSpecifiedInOrder(GameManagerObject.currentPlayer)) {

            boolean blocked = false;
            for (Card card : player.getHand().getCards()) {
                if (card instanceof Moat) {
                    blocked = true;
                    break;
                }
            }

            if (!blocked) {
                if (GameManagerObject.piles.get("Paladin.Model.CardTypes.Curse").getSize() > 0) {
                    player.getDeck().addCardToDiscard(GameManagerObject.piles.get("Paladin.Model.CardTypes.Curse").drawCard());
                }
            }

        }
    }
}
