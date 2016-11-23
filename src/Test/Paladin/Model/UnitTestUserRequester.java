package Test.Paladin.Model;

import Paladin.Controller.UserRequester;
import Paladin.Model.Card;

import java.util.ArrayList;

/**
 * Created by paulh on 11/19/2016.
 */
public class UnitTestUserRequester implements UserRequester {
    @Override
    public ArrayList<Card> askUserToSelectManyCards(ArrayList<Card> options, String message, String title, int min, int max) {
        return null;
    }

    public static int waitLength = 10000;

    @Override
    public Card askUserToSelectSingleCard(ArrayList<Card> options, String message, String title) {
        try {
            Thread.sleep(waitLength);
            System.out.println("Next request");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Card askUserToPlayTreasure(ArrayList<Card> options) {
        return null;
    }
}
