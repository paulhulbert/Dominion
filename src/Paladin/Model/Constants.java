package Paladin.Model;

import Paladin.Model.CardTypes.Copper;
import Paladin.Model.CardTypes.Estate;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by paulh on 10/8/2016.
 */
public class Constants {

    /**
     * A hash of all cards to their IDs for the purposes of message consumption
     */
    public static HashMap<Integer, Card> cards = new HashMap<>();


    /**
     * A hash of cards to their respective card types (Victory, treasure, action...)
     */
    public static HashMap<String, ArrayList<CardType>> cardTypes = new HashMap<>();

    public static int currentCardID = 0;


    /**
     * Hash of common names of cards to their respective classnames
     */
    public static HashMap<String, String> cardIdentifiers = new HashMap<>();



    /**
     *
     * @return new card ID
     */
    public static int getNewCardID() {
        return currentCardID++;
    }
}
