package Paladin.Model;

/**
 * Created by paulh on 10/9/2016.
 */
public class Message {
    public long time_created;
    public int GameID;
    public String Player;
    public String Details;

    public Message(long time_created, int gameID, String player, String details) {
        this.time_created = time_created;
        GameID = gameID;
        Player = player;
        Details = details;
    }
}
