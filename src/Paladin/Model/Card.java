package Paladin.Model;

import Paladin.Controller.Requester;
import Paladin.Model.CardTypes.Haggler;
import Paladin.Model.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 10/4/2016.
 */
public abstract class Card {


    protected int howManyShouldPileContain = 10;
    private int cost;

    private String name;

    private int ID;

    public Card(int cost, String name, int ID) {
        this.cost = cost;
        this.name = name;
        this.ID = ID;
        Constants.cards.put(ID, this);
    }


    public void onBuy(Turn turn) throws GameLogicException {
        int hagglerCount = 0;

        for (Card card : turn.getCardsPlayedThisTurn()) {
            if (card instanceof Haggler) {
                hagglerCount++;
            }
        }

        for (int i = 0; i < hagglerCount; i++) {
            ArrayList<Card> options = new ArrayList<>();

            for (String s : GameManagerObject.piles.keySet()) {
                SupplyPile pile = GameManagerObject.piles.get(s);
                if (pile.getSize() != 0 && pile.getTopCard().getCost() <= this.getCost() && !Constants.cardTypes.get(pile.getTopCard()).contains(CardType.VICTORY)) {
                    options.add(pile.getTopCard());
                }
            }
            if (options.isEmpty()) {
                return;
            }

            Card selected = Requester.askUserToSelectSingleCard(turn.currentPlayer, options,
                    "Choose card to gain", "Haggler");

            turn.gainCard(GameManagerObject.piles.get(selected.getClass().getName()).drawCard());
        }
    }

    public void onGain() {

    }

    public void playerGainedCard(Player player, Card card) {

    }

    /**
     * This action is implemented by individual cards.  They will check if the choices parameter
     * is null, and if it is, they will make all their requests to the UserRequester, and record the responses to be
     * sent to the database.  If it is not null, the cards will take their responses from the choices parameter.
     * @param turn
     *
     */
    public void onPlay(Turn turn) throws GameLogicException {

    }

    public void onTrash(Turn turn) throws GameLogicException {

    }

    public void onDiscard() {

    }

    public void onCleanup(ArrayList<Card> cardsToRemove) throws GameLogicException {

    }

    public int getVictoryPointWorth(Player owner) {
        return 0;
    }

    public void reactToAttack(Card causedBy, Player ownerOfReaction) {

    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }


    @Override
    public boolean equals(Object card) {
        if (card == null) {
            return false;
        }
        return this.getID() == ((Card) card).getID();
    }

    @Override
    public String toString() {
        return name;
    }
}
