package Paladin.Model.CardTypes;

import Paladin.Controller.Requester;
import Paladin.Model.*;

import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class Duchy extends Card {

    private static String name = "Duchy";
    static {
        ArrayList<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.VICTORY);
        Constants.cardTypes.put(name, cardTypes);
        Constants.cardIdentifiers.put(name, Card.class.getName().replace("Card", "CardTypes." + name));
    }
    public Duchy(int ID) {
        super(5, name, ID);
        howManyShouldPileContain = 8;
        if (GameManagerObject.players.size() > 2) {
            howManyShouldPileContain = 12;
        }
    }

    @Override
    public void onGain() {
        super.onGain();

        if (GameManagerObject.piles.containsKey(Duchess.class.getName()) &&
                GameManagerObject.piles.get(Duchess.class.getName()).getSize() > 0) {
            Player gainer = FrequentUseCardMethods.whoOwnsThisCard(this);

            ArrayList<String> options = new ArrayList<>();
            options.add("Yes");
            options.add("No");

            String wantToDiscard = Requester.askUserToSelectString(gainer, options,
                    "Do you want to gain a duchess with your duchy?", "Duchy");

            if (wantToDiscard.equals("Yes")) {
                gainer.getDeck().addCardToDiscard(GameManagerObject.piles.get(Duchess.class.getName()).drawCard(), true);
            }
        }
    }

    @Override
    public int getVictoryPointWorth(Player owner) {
        return super.getVictoryPointWorth(owner) + 3;
    }
}
