package Paladin.Controller;

import Paladin.Model.Card;
import Paladin.View.CardSelecter;

import java.util.ArrayList;

/**
 * Created by paulh on 11/19/2016.
 */
public class DesktopUserRequester implements UserRequester {
    @Override
    public Card askUserToSelectSingleCard(ArrayList<Card> options) {
        int originalSize = options.size();
        CardSelecter dialog = new CardSelecter(options);
        dialog.pack();
        dialog.setVisible(true);

        while (options.size() == originalSize) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return options.get(originalSize);
    }

    @Override
    public Card askUserToPlayTreasure(ArrayList<Card> options) {
        return null;
    }
}
