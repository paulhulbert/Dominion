package Paladin.Model;

import Paladin.Controller.DesktopUserRequester;
import Paladin.Controller.Requester;
import Paladin.Model.Exceptions.GameLogicException;
import com.google.gson.JsonElement;

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

    public Turn(Player currentPlayer) throws GameLogicException {
        this.currentPlayer = currentPlayer;
    }

    public void playTurn() throws GameLogicException {

        while (currentActions > 0 && phase == TurnPhase.ACTION) {
            if (GameManagerObject.localPlayer.equals(currentPlayer)) {
                ArrayList<Card> actionCardsInHand = new ArrayList<>();
                for (Card card : currentPlayer.getHand().getCards()) {
                    if (Constants.cardTypes.get(card.getName()).contains(CardType.ACTION)) {
                        actionCardsInHand.add(card);
                    }
                }
                if (actionCardsInHand.isEmpty()) {
                    break;
                }
                actionCardsInHand.add(null);
                Card cardToPlay = Requester.askUserToSelectSingleCard(currentPlayer, actionCardsInHand,
                        "Play an action card",
                        "Choose Action Card");
                if (cardToPlay == null) {
                    break;
                }
                playCard(cardToPlay, null);
            } else {
                //TODO: Write database reader code
            }
            GameManagerObject.uiInterface.update();
        }
        phase = TurnPhase.BUY;

        while (phase == TurnPhase.BUY) {
            if (GameManagerObject.localPlayer.equals(currentPlayer)) {
                ArrayList<Card> treasureCardsInHand = new ArrayList<>();
                for (Card card : currentPlayer.getHand().getCards()) {
                    if (Constants.cardTypes.get(card.getName()).contains(CardType.TREASURE)) {
                        treasureCardsInHand.add(card);
                    }
                }
                if (treasureCardsInHand.isEmpty()) {
                    break;
                }
                treasureCardsInHand.add(null);
                Card cardToPlay = Requester.askUserToSelectSingleCard(currentPlayer, treasureCardsInHand,
                        "Play a treasure card",
                        "Choose Treasure Card");
                if (cardToPlay == null) {
                    break;
                }
                playCard(cardToPlay, null);
            } else {
                //TODO: Write database reader code
            }
            GameManagerObject.uiInterface.update();
        }

        while (phase == TurnPhase.BUY && currentBuys > 0) {
            if (GameManagerObject.localPlayer.equals(currentPlayer)) {
                ArrayList<Card> buyOptions = new ArrayList<>();
                for (String s : GameManagerObject.piles.keySet()) {
                    SupplyPile pile = GameManagerObject.piles.get(s);
                    if (pile.getTopCard() != null && pile.getTopCard().getCost() <= currentMoney) {
                        buyOptions.add(pile.getTopCard());
                    }
                }
                if (buyOptions.isEmpty()) {
                    break;
                }
                buyOptions.add(null);
                Card cardToBuy = Requester.askUserToSelectSingleCard(currentPlayer, buyOptions,
                        "Choose a card to buy",
                        "Choose Deck");
                if (cardToBuy == null) {
                    break;
                }
                buyCard(cardToBuy.getClass().getName());
            } else {
                //TODO: Write database reader code
            }
            GameManagerObject.uiInterface.update();
        }
        phase = TurnPhase.CLEANUP;

        endTurn();
    }


    public void trashCard(Card card, JsonElement choices) throws GameLogicException {
        if (!currentPlayer.getHand().contains(card)) {
            throw new GameLogicException("Attempted to trash card not in hand");
        }

        card.onTrash(this, choices);

        currentPlayer.getHand().removeCard(card);

        GameManagerObject.trash.add(card);
    }


    public void playCard(Card card, JsonElement choices) throws GameLogicException {
        if (phase == TurnPhase.ACTION && currentActions <= 0) {
            throw new GameLogicException("Not enough actions.");
        }
        if (!currentPlayer.getHand().contains(card)) {
            throw new GameLogicException("Attempted to play card not in hand");
        }

        cardsPlayedThisTurn.add(card);
        currentPlayer.getHand().removeCard(card);
        card.onPlay(this, choices);

        if (phase == TurnPhase.ACTION) {
            currentActions--;
        }
    }

    public boolean buyCard(String name) {

        Card card = null;
        if (GameManagerObject.piles.containsKey(name)) {
            card = GameManagerObject.piles.get(name).getTopCard();
        } else {
            card = GameManagerObject.piles.get(Constants.cardIdentifiers.get(name)).getTopCard();
        }

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

    public void discardCard(Card card) {
        currentPlayer.getDeck().addCardToDiscard(card);
        currentPlayer.getHand().removeCard(card);
    }

    public void drawCard() throws GameLogicException {
        Card card = currentPlayer.getDeck().drawCard();
        if (card == null) {
            return;
        }
        currentPlayer.getHand().addCard(card);
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


    public void endTurn() throws GameLogicException {

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

    public TurnPhase getPhase() {
        return phase;
    }

    public void setPhase(TurnPhase phase) {
        this.phase = phase;
    }

    public int getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(int currentMoney) {
        this.currentMoney = currentMoney;
    }

    public int getCurrentActions() {
        return currentActions;
    }

    public void setCurrentActions(int currentActions) {
        this.currentActions = currentActions;
    }

    public int getCurrentBuys() {
        return currentBuys;
    }

    public void setCurrentBuys(int currentBuys) {
        this.currentBuys = currentBuys;
    }

    public ArrayList<Card> getCardsPlayedThisTurn() {
        return cardsPlayedThisTurn;
    }

    public void setCardsPlayedThisTurn(ArrayList<Card> cardsPlayedThisTurn) {
        this.cardsPlayedThisTurn = cardsPlayedThisTurn;
    }

    public ArrayList<Card> getCardsGainedThisTurn() {
        return cardsGainedThisTurn;
    }

    public void setCardsGainedThisTurn(ArrayList<Card> cardsGainedThisTurn) {
        this.cardsGainedThisTurn = cardsGainedThisTurn;
    }
}
