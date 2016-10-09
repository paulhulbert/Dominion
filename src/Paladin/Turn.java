package Paladin;

import Paladin.Exceptions.GameLogicException;

import java.util.ArrayList;

/**
 * Created by paulh on 10/4/2016.
 */
public class Turn {

    public TurnPhase phase = TurnPhase.ACTION;

    private int currentMoney = 0;
    private int currentActions = 1;
    private int currentBuys = 1;

    public Player currentPlayer;



    private ArrayList<Card> cardsPlayedThisTurn = new ArrayList<>();
    private ArrayList<Card> cardsGainedThisTurn = new ArrayList<>();

    public Turn(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }


    public void playCard(Card card) throws GameLogicException {
        if (phase == TurnPhase.ACTION && currentActions <= 0) {
            throw new GameLogicException("Not enough actions.");
        }
        card.onPlay(this);
        cardsPlayedThisTurn.add(card);
        currentPlayer.getHand().removeCard(card);

        if (phase == TurnPhase.ACTION) {
            currentActions--;
        }
    }

    public boolean buyCard(String name) {

        Card card = GameManagerObject.piles.get(name).getTopCard();

        if (card.getCost() > currentMoney) {
            return false;
        }
        removeMoney(card.getCost());

        card.onBuy(this);
        cardsGainedThisTurn.add(card);
        GameManagerObject.piles.get(name).removeCard(card);
        currentBuys--;

        return true;

    }

    public void addMoney(int amount) {
        currentMoney += amount;
    }

    public boolean removeMoney(int amount) {
        if (amount > currentMoney) {
            return false;
        }
        currentMoney -= amount;
        return true;
    }


    public void endTurn() {

        ArrayList<Card> currentHand = currentPlayer.getHand().getCards();

        for (Card card : currentHand) {
            currentPlayer.getDeck().addCardToDiscard(card);
        }
        currentHand.clear();

        for (Card card : cardsGainedThisTurn) {
            currentPlayer.getDeck().addCardToDiscard(card);
        }

        for (Card card : cardsPlayedThisTurn) {
            currentPlayer.getDeck().addCardToDiscard(card);
        }


        for (int i = 0; i < 5; i++) {
            currentPlayer.getHand().addCard(currentPlayer.getDeck().drawCard());
        }

        try {
            GameManagerObject.endturn();
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

}
