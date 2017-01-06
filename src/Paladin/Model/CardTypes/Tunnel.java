package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;

import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class Tunnel extends Card {

    private static String name = "Tunnel";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.VICTORY);
        cardTypes.add(CardType.REACTION);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Tunnel(int ID) {
        super(3, name, ID);
        howManyShouldPileContain = 8;
        if (GameManagerObject.players.size() > 2) {
            howManyShouldPileContain = 12;
        }
    }

    @Override
    public void onDiscard() {
        super.onDiscard();

        ArrayList<String> options = new ArrayList<>();
        options.add("Yes");
        options.add("No");

        Player player = FrequentUseCardMethods.whoOwnsThisCard(this);

        String wantToReveal = Requester.askUserToSelectString(player, options,
                "Do you want to reveal your tunnel and gain a gold?", "Tunnel");

        if (wantToReveal.equals("Yes")) {
            player.getDeck().addCardToDiscard(GameManagerObject.piles.get(Gold.class.getName()).drawCard(), true);
        }
    }

    @Override
    public int getVictoryPointWorth(Player owner) {
        return super.getVictoryPointWorth(owner) + 2;
    }
}
