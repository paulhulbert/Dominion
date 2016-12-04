package Paladin.Model;

import Paladin.Model.CardTypes.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by paulh on 10/8/2016.
 */
public class Constants {
    
    public static ArrayList<String> baseSetNames = new ArrayList<>();
    
    static {
        baseSetNames.add(Village.class.getName());
        baseSetNames.add(Woodcutter.class.getName());
        baseSetNames.add(Smithy.class.getName());
        baseSetNames.add(Cellar.class.getName());
        baseSetNames.add(Moat.class.getName());
        baseSetNames.add(Workshop.class.getName());
        baseSetNames.add(Chancellor.class.getName());
        baseSetNames.add(Remodel.class.getName());
        baseSetNames.add(Market.class.getName());
        baseSetNames.add(Militia.class.getName());
        baseSetNames.add(Mine.class.getName());
        baseSetNames.add(Festival.class.getName());
        baseSetNames.add(Adventurer.class.getName());
        baseSetNames.add(Bureaucrat.class.getName());
        baseSetNames.add(CouncilRoom.class.getName());
        baseSetNames.add(Feast.class.getName());
        baseSetNames.add(Gardens.class.getName());
        baseSetNames.add(Laboratory.class.getName());
        baseSetNames.add(Library.class.getName());
        baseSetNames.add(ThroneRoom.class.getName());
        baseSetNames.add(Moneylender.class.getName());
        baseSetNames.add(Witch.class.getName());
        baseSetNames.add(Thief.class.getName());
        baseSetNames.add(Spy.class.getName());
    }

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
