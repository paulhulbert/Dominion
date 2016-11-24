package Paladin.Model;

import Paladin.Controller.UserRequester;
import Paladin.Model.CardTypes.*;
import Paladin.Model.Exceptions.GameLogicException;
import Paladin.View.UIInterface;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by paulh on 10/4/2016.
 */
public class GameManagerObject {

    public static Random seededRandom = new Random();

    public static long seed = -1;

    public static boolean gameOver = false;

    public static int gameID = -1;

    public static UserRequester userRequester;
    public static UIInterface uiInterface;

    public static Player host;
    public static Player localPlayer;
    public static Player currentPlayer;
    public static ArrayList<Player> players = new ArrayList<>();

    public static HashMap<String, SupplyPile> piles = new HashMap<>();

    public static ArrayList<Turn> turns = new ArrayList<>();

    public static ArrayList<Card> trash = new ArrayList<>();

    static {
        DatabaseManager.startup();
    }

    public static void joinGame(int ID, Player hostPlayer, long seedValue) {
        gameID = ID;
        host = hostPlayer;
        seed = seedValue;
    }


    public static void setupGame(boolean isHost) throws GameLogicException {
        Constants.currentCardID = 0;
        localPlayer = new Player("Paul");
        if (isHost) {
            host = localPlayer;
            seed = System.currentTimeMillis();

            gameID = seededRandom.nextInt(100000);
        }
        seededRandom = new Random(seed);
        players.clear();
        players.add(localPlayer);

        piles.clear();

        addPile(Copper.class.getName(), 100);
        addPile(Estate.class.getName(), 8);
        addPile(Village.class.getName(), 10);
        addPile(Woodcutter.class.getName(), 10);
        addPile(Smithy.class.getName(), 10);
        addPile(Cellar.class.getName(), 10);
        addPile(Moat.class.getName(), 10);
        addPile(Workshop.class.getName(), 10);
        addPile(Remodel.class.getName(), 10);
        addPile(Duchy.class.getName(), 8);
        addPile(Gold.class.getName(), 30);
        addPile(Market.class.getName(), 10);
        addPile(Militia.class.getName(), 10);
        addPile(Mine.class.getName(), 10);
        addPile(Province.class.getName(), 8);
        addPile(Silver.class.getName(), 60);

        turns.clear();


        System.out.println("Game is set up.");
    }


    public static void startGame() throws GameLogicException {
        currentPlayer = players.get(0);
        Turn turn = new Turn(currentPlayer);
        turns.add(turn);
        uiInterface.update();
        turn.playTurn();
    }

    public static void addPile(String name) {
        piles.put(name, new SupplyPile(name));
    }

    public static void addPile(String name, int amount) {
        piles.put(name, new SupplyPile(name, amount));
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static void addHost(Player player) {
        players.add(player);
        host = player;
    }

    public static Turn getCurrentTurn() {
        return turns.get(turns.size()-1);
    }

    public static void endturn() throws GameLogicException {
        if (gameOver) {
            return;
        }
        if (players.isEmpty()) {
            throw new GameLogicException("Players list was empty");
        }
        Player current = players.remove(0);
        players.add(current);
        currentPlayer = players.get(0);
        turns.add(new Turn(currentPlayer));
        uiInterface.update();
        turns.get(turns.size() - 1).playTurn();

    }

    public static void addMessage(Message message) throws GameLogicException {
        MessageHandler.handleMessage(message);
        //TODO:  Add database code here
    }


    public static void main(String[] args) {

        try {

            setupGame(true);
            System.out.println("hello");
            startGame();
            addMessage(new Message(1l,1,"Paul","{ \"time_created\":1133442, \"GameID\":115, \"Play" +
                    "er\":\"Paul\", \"Details\":{ \"type\":\"play\", \"cardID\":\"1\" } }"));

            turns.get(0).endTurn();
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
        System.out.println("there");
    }



}
