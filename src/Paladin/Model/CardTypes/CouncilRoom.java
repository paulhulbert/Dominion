package Paladin.Model.CardTypes;

import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 11/22/2016.
 */
public class CouncilRoom extends Card {

    private static String name = "CouncilRoom";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.ACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public CouncilRoom(int ID) {
        super(5, name, ID);
    }

    @Override
    public void onPlay(Turn turn) throws GameLogicException {
        super.onPlay(turn);
        turn.drawCard();
        turn.drawCard();
        turn.drawCard();
        turn.drawCard();
        turn.setCurrentBuys(turn.getCurrentBuys() + 1);

        for (Player player : GameManagerObject.getPlayersAsideFromSpecifiedInOrder(turn.currentPlayer)) {
            player.getHand().addCard(player.getDeck().drawCard());
        }
    }
}
