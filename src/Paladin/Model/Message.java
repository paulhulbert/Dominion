package Paladin.Model;

import com.google.gson.JsonElement;

import java.util.HashMap;

/**
 * Created by paulh on 10/9/2016.
 */
public class Message {
    public long time_created;
    public int GameID;
    public String Player;
    public String Details;

    private HashMap<String, String> detailsMap = new HashMap<>();
    private HashMap<String, String> detailsMapOfArrays = new HashMap<>();


    public Message(long time_created, int gameID, String player, String details) {
        this.time_created = time_created;
        GameID = gameID;
        Player = player;
        Details = details;
    }

    private void updateFromMaps() {
        Details = "\"Details\":{";

        for (String string : detailsMap.keySet()) {
            Details += "\"" + string + "\":\"" + detailsMap.get(string) + "\",";
        }

        for (String string : detailsMapOfArrays.keySet()) {
            Details += "\"" + string + "\":" + detailsMapOfArrays.get(string) + ",";
        }

        Details = Details.substring(0,Details.length() - 1) + "}";
    }

    public void put(String key, String value) {
        detailsMap.put(key, value);
        updateFromMaps();
    }

    public void putArray(String key, String value) {
        detailsMapOfArrays.put(key, value);
        updateFromMaps();
    }

    public static JsonElement convertToJSon(Message message) {



        return null;
    }
}
