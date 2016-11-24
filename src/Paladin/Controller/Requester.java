package Paladin.Controller;

import Paladin.Model.*;
import com.google.gson.JsonElement;

import java.util.ArrayList;

/**
 * Created by paulh on 11/23/2016.
 */
public class Requester {



    public static Card askUserToSelectSingleCard(Player target, ArrayList<Card> options, String message, String title) {

        if (target.equals(GameManagerObject.localPlayer)) {
            Card selected =  GameManagerObject.userRequester.askUserToSelectSingleCard(options, message, title);

            String details = "\"Details\":{ \"type\":\"cardSelect\",\"cards\":[\"" + selected.getID() + "\"]}";

            Message newMessage = new Message(System.currentTimeMillis(),
                    GameManagerObject.gameID, target.getName(), details);
            DatabaseManager.sendMessage(newMessage);

            return selected;
        } else {

            if (target.equals(GameManagerObject.currentPlayer)) {
                JsonElement jsonElement = MessageHandler.getNextCardSelect();

                String cards = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("cards").getAsString();

                String cardID = cards.replace("[","").replace("]","").replace("\"","");

                Card selected = Constants.cards.get(Integer.parseInt(cardID));

                return selected;
            } else {

            }
        }

        return null;
    }

    public static ArrayList<Card> askUserToSelectManyCards(Player target, ArrayList<Card> options, String message,
                                                           String title, int min, int max) {

        if (target.equals(GameManagerObject.localPlayer)) {
            ArrayList<Card> selected =  GameManagerObject.userRequester.askUserToSelectManyCards(options, message, title, min, max);

            String details = "\"Details\":{ \"type\":\"cardSelect\",\"cards\":[\"";

            for (Card card : selected) {
                details += "\"" + card.getID() + "\",";
            }

            details = details.substring(0,details.length() - 2) + "\"]}";

            Message newMessage = new Message(System.currentTimeMillis(),
                    GameManagerObject.gameID, target.getName(), details);
            DatabaseManager.sendMessage(newMessage);

            return selected;
        } else {

            if (target.equals(GameManagerObject.currentPlayer)) {
                JsonElement jsonElement = MessageHandler.getNextCardSelect();

                String cards = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("cards").getAsString();

                String[] cardsTokens = cards.replace("[","").replace("]","").replace("\"","").split(",");

                ArrayList<Card> selected = new ArrayList<>();

                for (String s : cardsTokens) {
                    int cardID = Integer.parseInt(s);
                    selected.add(Constants.cards.get(cardID));
                }

                return selected;
            } else {

            }
        }

        return null;
    }

}
