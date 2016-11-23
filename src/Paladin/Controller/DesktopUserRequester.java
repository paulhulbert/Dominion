package Paladin.Controller;

import Paladin.Model.Card;
import Paladin.View.CardSelecter;

import java.util.ArrayList;

/**
 * Created by paulh on 11/19/2016.
 */
public class DesktopUserRequester implements UserRequester {

    @Override
    public ArrayList<Card> askUserToSelectManyCards(ArrayList<Card> options, String message, String title, int min, int max) {

        ArrayList<Card> selections = new ArrayList<>();

        ArrayList<Card> tempOptions = new ArrayList<>();

        for (Card card : options) {
            tempOptions.add(card);
        }

        while (selections.size() < max && !tempOptions.isEmpty()) {
            String selectedString = "  -  Already selected: ";
            for (Card card : selections) {
                selectedString += card.getName() + ", ";
            }
            selectedString = selectedString.substring(0, selectedString.length() - 2);
            Card selection = askUserToSelectSingleCard(tempOptions, message + selectedString, title);
            if (selection == null) {
                if (selections.size() >= min) {
                    break;
                } else {
                    continue;
                }
            }
            selections.add(selection);
            tempOptions.remove(selection);
        }


        return selections;
    }

    @Override
    public Card askUserToSelectSingleCard(ArrayList<Card> options, String message, String title) {
        int originalSize = options.size();
        CardSelecter dialog = new CardSelecter(options, message, title);
        dialog.pack();
        dialog.setVisible(true);

        while (options.size() == originalSize) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Card selection = options.remove(originalSize);


        return selection;
    }

    @Override
    public Card askUserToPlayTreasure(ArrayList<Card> options) {
        return null;
    }
}
