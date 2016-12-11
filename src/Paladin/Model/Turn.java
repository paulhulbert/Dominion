package Paladin.Model;

import Paladin.Controller.Requester;
import Paladin.Model.Exceptions.GameLogicException;

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
            playCard(cardToPlay);

            GameManagerObject.uiInterface.update();
        }
        phase = TurnPhase.BUY;

        while (phase == TurnPhase.BUY) {
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
            playCard(cardToPlay);
            GameManagerObject.uiInterface.update();
        }

        while (phase == TurnPhase.BUY && currentBuys > 0) {
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
            GameManagerObject.uiInterface.update();
        }
        phase = TurnPhase.CLEANUP;

        ArrayList<Card> handCardsToRemove = new ArrayList<>();
        for (Card card : currentPlayer.getHand().getCards()) {
            card.onCleanup(handCardsToRemove);
        }
        for (Card card : handCardsToRemove) {
            currentPlayer.getHand().getCards().remove(card);
        }

        ArrayList<Card> playedCardsToRemove = new ArrayList<>();
        for (Card card : cardsPlayedThisTurn) {
            card.onCleanup(playedCardsToRemove);
        }
        for (Card card : playedCardsToRemove) {
            cardsPlayedThisTurn.remove(card);
        }

        ArrayList<Card> gainedCardsToRemove = new ArrayList<>();
        for (Card card : cardsGainedThisTurn) {
            card.onCleanup(gainedCardsToRemove);
        }
        for (Card card : gainedCardsToRemove) {
            cardsGainedThisTurn.remove(card);
        }

        endTurn();
    }


    public void trashCard(Card card) throws GameLogicException {
        if (!currentPlayer.getHand().contains(card)) {
            throw new GameLogicException("Attempted to trash card not in hand");
        }

        card.onTrash(this);

        currentPlayer.getHand().removeCard(card);

        GameManagerObject.trash.add(card);
    }


    public void playCard(Card card) throws GameLogicException {
        if (phase == TurnPhase.ACTION && currentActions <= 0) {
            throw new GameLogicException("Not enough actions.");
        }
        if (!currentPlayer.getHand().contains(card)) {
            throw new GameLogicException("Attempted to play card not in hand");
        }

        cardsPlayedThisTurn.add(card);
        currentPlayer.getHand().removeCard(card);
        card.onPlay(this);

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
        gainCard(card);
        GameManagerObject.piles.get(name).removeCard(card);
        currentBuys--;

        return true;

    }

    public void drawCard() throws GameLogicException {
        Card card = currentPlayer.getDeck().drawCard();
        if (card == null) {
            return;
        }
        currentPlayer.getHand().addCard(card);
    }

    public void gainCard(Card card) {
        cardsGainedThisTurn.add(card);


        card.onGain();


        ArrayList<Player> players = GameManagerObject.getPlayersAsideFromSpecifiedInOrder(currentPlayer);
        players.add(0, currentPlayer);

        for (Player player : players) {
            for (Card cardInHand : player.getHand().getCards()) {
                cardInHand.playerGainedCard(currentPlayer, card);
            }
        }
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
            currentPlayer.getDeck().addCardToDiscard(card, false);
        }
        currentHand.clear();

        for (Card card : cardsGainedThisTurn) {
            currentPlayer.getDeck().addCardToDiscard(card, false);
        }

        for (Card card : cardsPlayedThisTurn) {
            if (GameManagerObject.trash.contains(card)) {
                continue;
            }
            currentPlayer.getDeck().addCardToDiscard(card, false);
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
