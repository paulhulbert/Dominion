package Test.Paladin.Model;

import Paladin.Controller.UserRequester;
import Paladin.Model.Card;

import java.util.ArrayList;

/**
 * Created by paulh on 11/19/2016.
 */
public class UnitTestUserRequester implements UserRequester {
    @Override
    public Card askUserToSelectSingleCard(ArrayList<Card> options) {
        return null;
    }

    @Override
    public Card askUserToPlayTreasure(ArrayList<Card> options) {
        return null;
    }
}
