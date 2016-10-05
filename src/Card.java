import java.util.ArrayList;

/**
 * Created by paulh on 10/4/2016.
 */
public class Card {


    private int cost;

    private ArrayList<CardType> cardTypes;

    private String name;

    private int ID;


    public void onBuy(Turn turn) {

    }

    public void onPlay(Turn turn) {

    }









    public Card(int cost, ArrayList<CardType> cardTypes, String name, int ID) {
        this.cost = cost;
        this.cardTypes = cardTypes;
        this.name = name;
        this.ID = ID;
    }

    public int getCost() {
        return cost;
    }

    public ArrayList<CardType> getCardTypes() {
        return cardTypes;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }


    @Override
    public boolean equals(Object card) {
        return this.getID() == ((Card) card).getID();
    }


}
