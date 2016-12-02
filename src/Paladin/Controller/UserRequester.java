package Paladin.Controller;

import Paladin.Model.Card;

import java.util.ArrayList;

/**
 * This class is the interface for a class that will ask the user for an input via a GUI
 *
 * Created by paulh on 11/19/2016.
 */
public interface UserRequester {

    @Deprecated
    Card askUserToSelectSingleCard(ArrayList<Card> options, String message, String title);

    ArrayList<Card> askUserToSelectManyCards(ArrayList<Card> options, String message, String title, int min, int max);

    Card askUserToPlayTreasure(ArrayList<Card> options);

    String askUserToSelectString(ArrayList<String> options, String message, String title);
}
