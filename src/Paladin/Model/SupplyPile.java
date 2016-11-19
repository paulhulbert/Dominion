package Paladin.Model;

import Paladin.Model.CardTypes.Copper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by paulh on 10/8/2016.
 */
public class SupplyPile {

    private ArrayList<Card> cards = new ArrayList<>();

    private int maxInDeck = 10;

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
    }


    public Card getTopCard() {
        return cards.get(0);
    }

    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    public static void main(String[] args) {
        SupplyPile supplyPile = new SupplyPile(Copper.class.getName(), 50);
    }
}
