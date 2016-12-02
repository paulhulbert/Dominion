package Paladin.Model;

import Paladin.Model.Exceptions.GameLogicException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Created by paulh on 10/9/2016.
 */
public class MessageHandler {

    public static Queue<JsonElement> cardSelects = new ArrayDeque<>();
    public static Queue<String> playerJoins = new ArrayDeque<>();

    public static HashMap<Integer, Long> seedMap = new HashMap<>();

    public static HashMap<String, JsonElement> remoteRequests = new HashMap<>();



    /**
     * Handles the message that is received from the database
     * @param message
     */
    public static void handleMessage(Message message) throws GameLogicException {
        JsonElement jsonElement = new JsonParser().parse("{" + message.Details + "}");

        System.out.println("Got a message: " + message.Details);

        String type = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("type").getAsString();

        if (type.equals("cardSelect")) {
            if (!message.Player.equals(GameManagerObject.localPlayer.getName())) {
                cardSelects.add(jsonElement);
            }
        }

        if (type.equals("remoteStringSelectResponse")) {
            remoteRequests.put(jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("uniqueId").getAsString(),
                    jsonElement);
        }

        if (type.equals("remoteStringSelectRequest")) {

            if (jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("target").
                    getAsString().equals(GameManagerObject.localPlayer.getName())) {


                JsonArray cards = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("options").getAsJsonArray();

                ArrayList<String> options = new ArrayList<>();

                for (JsonElement element : cards) {
                    options.add(element.getAsString());
                }

                String messageText = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("message").getAsString();
                String title = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("title").getAsString();

                String selected = GameManagerObject.userRequester.askUserToSelectString(options, messageText, title);


                String cardsSelected = "[\"" + selected + "\"]";



                Message newMessage = new Message(System.currentTimeMillis(),
                        GameManagerObject.gameID, GameManagerObject.currentPlayer.getName(), "");
                newMessage.put("type", "remoteStringSelectResponse");
                newMessage.put("uniqueId", jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("uniqueId").getAsString());
                newMessage.putArray("selected", cardsSelected);
                DatabaseManager.sendMessage(newMessage);

            }
        }


        if (type.equals("remoteCardSelectResponse")) {
            remoteRequests.put(jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("uniqueId").getAsString(),
                    jsonElement);
        }

        if (type.equals("remoteCardSelectRequest")) {

            if (jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("target").
                    getAsString().equals(GameManagerObject.localPlayer.getName())) {


                JsonArray cards = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("cards").getAsJsonArray();

                ArrayList<Card> options = new ArrayList<>();

                for (JsonElement element : cards) {
                    int cardID = element.getAsInt();
                    options.add(Constants.cards.get(cardID));
                }

                String messageText = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("message").getAsString();
                String title = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("title").getAsString();
                int min = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("min").getAsInt();
                int max = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("max").getAsInt();

                ArrayList<Card> selected = GameManagerObject.userRequester.askUserToSelectManyCards(options, messageText, title, min, max);


                String cardsSelected = "[";


                for (Card card : selected) {
                    cardsSelected += "\"" + card.getID() + "\",";
                }

                cardsSelected = cardsSelected.substring(0,cardsSelected.length() - 2) + "\"]";

                Message newMessage = new Message(System.currentTimeMillis(),
                        GameManagerObject.gameID, GameManagerObject.currentPlayer.getName(), "");
                newMessage.put("type", "remoteCardSelectResponse");
                newMessage.put("uniqueId", jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("uniqueId").getAsString());
                newMessage.putArray("cards", cardsSelected);
                DatabaseManager.sendMessage(newMessage);

            }
        }

        if (type.equals("playerJoin")) {
            playerJoins.add(message.Player);
        }

        if (type.equals("startTheGameAlready")) {
            GameManagerObject.started = true;
        }

        if (type.equals("createGame")) {
            int gameID = message.GameID;
            long seed = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("seed").getAsLong();

            seedMap.put(gameID, seed);

        }

        if (type.equals("buy")) {
            //buy(jsonElement);
        }

        if (type.equals("play")) {
            //play(jsonElement);
        }

        if (type.equals("shuffle")) {
            //shuffle(jsonElement);
        }
    }


    public static JsonElement getNextCardSelect() {
        while (cardSelects.isEmpty()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return cardSelects.remove();
    }

    public static Queue<String> getPlayerJoins() {
        return playerJoins;
    }

    /**
     * Seed the shuffle for a certain player
     * Ex:
     *

     {
         "time_created":1133442,
         "GameID":115,
         "Player":"Paul",
         "Details":{
             "type":"shuffle",
             "target":"David",
             "newDeck":"[45,23,67,24,7,5,22,56,88]"   //Last card is top of the deck
         }
     }

     *
     * @param jsonElement

    public static void shuffle(JsonElement jsonElement) throws GameLogicException {
        Turn currentTurn = GameManagerObject.turns.get(0);

        String targetName = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("target").getAsString();

        Player targetPlayer = null;

        for (Player player : GameManagerObject.players) {
            if (player.getName().equals(targetName)) {
                targetPlayer = player;
                break;
            }
        }

        if (targetPlayer == null) {
            throw new GameLogicException("Attempted to seed a shuffle for unknown player with name: " + targetName);
        }

        ArrayList<Integer> seed = new ArrayList<>();

        String array = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("newDeck").getAsString();

        array = array.substring(1,array.length()-1);

        String[] tokens = array.split(",");

        for (String s : tokens) {
            seed.add(Integer.parseInt(s));
        }

        targetPlayer.getDeck().setShuffleSeed(seed);


    }
    */

    /**
     * Play a card from the players hand with a specified ID
     * Ex:

     {
        "time_created":1133442,
        "GameID":115,
        "Player":"Paul",
        "Details":{
            "type":"play",
            "cardID":430,
            "choices":"<choices>"
        }
     }

     * @param jsonElement
     */
    public static void play(JsonElement jsonElement) {
        Turn currentTurn = GameManagerObject.turns.get(0);

        int cardID = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("cardID").getAsInt();

        try {
            currentTurn.playCard(Constants.cards.get(cardID));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }

    }


    /**
     * Buy a card with a specified name
     * Ex:

     {
         "time_created":1133442,
         "GameID":115,
         "Player":"Paul",
         "Details":{
             "type":"buy",
             "cardName":"Copper"
         }
     }

     * @param jsonElement
     */
    private static void buy(JsonElement jsonElement) {
        Turn currentTurn = GameManagerObject.turns.get(0);

        String cardName = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("cardName").getAsString();

        if (GameManagerObject.piles.containsKey(cardName)) {
            currentTurn.buyCard(cardName);
        } else {
            currentTurn.buyCard(Constants.cardIdentifiers.get(cardName));
        }

    }


    public static void main(String[] args) {
        try {
            handleMessage(new Message(1l,1,"hi","{ \"data\" : { \"field1\" : \"value1\", \"field2\" : \"value2\" } }"));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }
}
