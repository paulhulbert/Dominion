package Paladin.Controller;

import Paladin.Model.Card;

/**
 * This class is the interface for a class that will ask the user for an input via a GUI
 *
 * Created by paulh on 11/19/2016.
 */
public interface UserRequester {

    Card askUserToPlayAction();

    Card askUserToPlayTreasure();
}