package Paladin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by paulh on 10/8/2016.
 */
public class Constants {

    public static HashMap<String, ArrayList<CardType>> cardTypes = new HashMap<>();

     static int currentCardID = 0;


    public static int getNewCardID() {
        return currentCardID++;
    }
}
