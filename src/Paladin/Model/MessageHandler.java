package Paladin.Model;

import Paladin.Model.Exceptions.GameLogicException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Created by paulh on 10/9/2016.
 */
public class MessageHandler {


    /**
     * Handles the message that is received from the database
     * @param message
     */
    public static void handleMessage(Message message) throws GameLogicException {
        JsonElement jsonElement = new JsonParser().parse(message.Details);

        String type = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("type").getAsString();

        if (type.equals("buy")) {
            buy(jsonElement);
        }

        if (type.equals("play")) {
            play(jsonElement);
        }

        if (type.equals("shuffle")) {
            shuffle(jsonElement);
        }
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
     */
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
            currentTurn.playCard(Constants.cards.get(cardID), jsonElement);
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
