package Paladin.Controller;

import Paladin.Model.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

/**
 * Created by paulh on 11/23/2016.
 */
public class Requester {



    public static Card askUserToSelectSingleCard(Player target, ArrayList<Card> options, String message, String title) {
        /*
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
        } */

        ArrayList<Card> selected = askUserToSelectManyCards(target, options, message, title, 0, 1);

        if (selected.isEmpty()) {
            return null;
        }

        return selected.get(0);
    }

    public static ArrayList<Card> askUserToSelectManyCards(Player target, ArrayList<Card> options, String message,
                                                           String title, int min, int max) {

        // You asking yourself
        if (target.equals(GameManagerObject.localPlayer) && target.equals(GameManagerObject.currentPlayer)) {
            ArrayList<Card> selected =  GameManagerObject.userRequester.askUserToSelectManyCards(options, message, title, min, max);


            Message newMessage = new Message(System.currentTimeMillis(),
                    GameManagerObject.gameID, target.getName(), "");
            newMessage.put("type", "cardSelect");
            if (selected.isEmpty()) {
                newMessage.putArray("cards", "[]");
            } else {
                String details = "[";

                for (Card card : selected) {
                    details += "\"" + card.getID() + "\",";
                }

                details = details.substring(0,details.length() - 2) + "\"]";
                newMessage.putArray("cards", details);
            }


            DatabaseManager.sendMessage(newMessage);

            return selected;
        }
        //Them asking themselves
        else {

            if (target.equals(GameManagerObject.currentPlayer)) {
                JsonElement jsonElement = MessageHandler.getNextCardSelect();

                JsonArray cards = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("cards").getAsJsonArray();

                ArrayList<Card> selected = new ArrayList<>();

                for (JsonElement element : cards) {
                    int cardID = element.getAsInt();
                    selected.add(Constants.cards.get(cardID));
                }

                return selected;
            }
            //Someone asking someone else
            else {

                String uniqueId = "" + GameManagerObject.turns.size() + "-" +
                        GameManagerObject.getCurrentTurn().getCardsPlayedThisTurn().size();

                String cardOptions = "[";


                for (Card card : options) {
                    uniqueId += "-" + card.getID();
                    cardOptions += "\"" + card.getID() + "\",";
                }

                cardOptions = cardOptions.substring(0,cardOptions.length() - 2) + "\"]";

                //You are asking someone else
                if (GameManagerObject.currentPlayer.equals(GameManagerObject.localPlayer)) {
                    Message newMessage = new Message(System.currentTimeMillis(),
                            GameManagerObject.gameID, GameManagerObject.currentPlayer.getName(), "");
                    newMessage.put("type", "remoteCardSelectRequest");
                    newMessage.put("target", target.getName());
                    newMessage.put("message", message);
                    newMessage.put("title", title);
                    newMessage.put("uniqueId", uniqueId);
                    newMessage.putArray("cards", cardOptions);
                    newMessage.put("min", "" + min);
                    newMessage.put("max", "" + max);
                    DatabaseManager.sendMessage(newMessage);
                }


                while (!MessageHandler.remoteRequests.containsKey(uniqueId)) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                JsonElement jsonElement = MessageHandler.remoteRequests.get(uniqueId);

                JsonArray cards = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("cards").getAsJsonArray();

                ArrayList<Card> selected = new ArrayList<>();

                for (JsonElement element : cards) {
                    int cardID = element.getAsInt();
                    selected.add(Constants.cards.get(cardID));
                }

                return selected;


            }
        }

    }

}