package Paladin.Model.CardTypes;

import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class Militia extends Card {
    private static String name = "Militia";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        cardTypes.add(CardType.ATTACK);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Militia(int ID) {
        super(4, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.addMoney(2);
        ArrayList<Player> players = GameManagerObject.getPlayersAsideFromSpecifiedInOrder(GameManagerObject.currentPlayer);

        for (Player player : players) {

            boolean blocked = false;
            for (Card card : player.getHand().getCards()) {
                if (card instanceof Moat) {
                    blocked = true;
                    break;
                }
            }

            if (!blocked) {
                FrequentUseCardMethods.forcePlayerToDiscardDownToX(3, player);
            }

        }


    }
}
