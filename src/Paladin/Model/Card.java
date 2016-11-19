package Paladin.Model;

import com.google.gson.JsonElement;

/**
 * Created by paulh on 10/4/2016.
 */
public abstract class Card {


    private int cost;

    private String name;

    private int ID;


    public void onBuy(Turn turn) {

    }

    public void onPlay(Turn turn, JsonElement choices) {

    }









    public Card(int cost, String name, int ID) {
        this.cost = cost;
        this.name = name;
        this.ID = ID;
        Constants.cards.put(ID, this);
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
        return this.getID() == ((Card) card).getID();
    }


}
