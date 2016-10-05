import java.util.ArrayList;

/**
 * Created by paulh on 10/4/2016.
 */
public class Turn {

    private int currentMoney = 0;
    private int currentActions = 1;
    private int currentBuys = 1;

    public Player currentPlayer;



    private ArrayList<Card> cardsPlayedThisTurn = new ArrayList<>();
    private ArrayList<Card> cardsBoughtThisTurn = new ArrayList<>();

    public Turn(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }


    public void playCard(Card card) {
        card.onPlay(this);
        cardsPlayedThisTurn.add(card);
    }

    public boolean buyCard(Card card) {

        if (card.getCost() > currentMoney) {
            return false;
        }
        currentMoney -= card.getCost();

        card.onBuy(this);
        cardsBoughtThisTurn.add(card);

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

}
