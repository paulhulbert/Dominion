package Paladin.Model;

import Paladin.Model.CardTypes.Copper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class SupplyPile {

    /**
     * Top of deck is last card
     */
    private ArrayList<Card> cards = new ArrayList<>();

    private int maxInDeck = 10;

    private String name;

    public SupplyPile(String cardName) {
        generatePile(cardName);
    }


    public SupplyPile(String cardName, int maxInDeck) {
        this.maxInDeck = maxInDeck;
        generatePile(cardName);
    }


    private void generatePile(String cardName) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            Class loadedClass = classLoader.loadClass(cardName);

            Constructor constructor = loadedClass.getConstructors()[0];


            for (int i = 0; i < maxInDeck; i++) {
                cards.add((Card)constructor.newInstance(Constants.getNewCardID()));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        name = cards.get(0).getName();
    }


    public Card getTopCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(0);
    }

    public Card drawCard() {
        return cards.remove(cards.size()-1);
    }

    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return cards.size();
    }

    public static void main(String[] args) {
        SupplyPile supplyPile = new SupplyPile(Copper.class.getName(), 50);
    }
}
