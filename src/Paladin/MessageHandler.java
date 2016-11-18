package Paladin;

import Paladin.Exceptions.GameLogicException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by paulh on 10/9/2016.
 */
public class MessageHandler {

    public static void handleMessage(Message message) {
        JsonElement jsonElement = new JsonParser().parse(message.Details);

        String type = jsonElement.getAsJsonObject().get("Details").getAsJsonObject().get("type").getAsString();

        if (type.equals("buy")) {
            buy(jsonElement);
        }

        if (type.equals("play")) {
            play(jsonElement);
        }
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

     *
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
        handleMessage(new Message(1l,1,"hi","{ \"data\" : { \"field1\" : \"value1\", \"field2\" : \"value2\" } }"));
    }
}
