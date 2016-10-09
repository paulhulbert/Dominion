package Paladin;

import Paladin.CardTypes.Copper;
import Paladin.CardTypes.Estate;
import Paladin.Exceptions.GameLogicException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by paulh on 10/4/2016.
 */
public class GameManagerObject {

    public static Player host;
    public static Player localPlayer;
    public static Player currentPlayer;
    public static ArrayList<Player> players = new ArrayList<>();

    public static HashMap<String, SupplyPile> piles = new HashMap<>();

    public static ArrayList<Turn> turns = new ArrayList<>();


    public static void setupGame(boolean isHost) {
        localPlayer = new Player();
        if (isHost) {
            host = localPlayer;
        }
        players.add(localPlayer);

        addPile(Copper.class.getName(), 100);
        addPile(Estate.class.getName(), 12);


        System.out.println("Game is set up.");
    }


    public static void startGame() {
        currentPlayer = players.get(0);
        Turn turn = new Turn(currentPlayer);
        turns.add(turn);
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

    public static void endturn() throws GameLogicException {
        if (players.isEmpty()) {
            throw new GameLogicException("Players list was empty");
        }
        Player current = players.remove(0);
        players.add(current);
        currentPlayer = players.get(0);
        turns.add(new Turn(currentPlayer));

    }


    public static void main(String[] args) {
        setupGame(true);
        System.out.println("hello");
        startGame();
        turns.get(0).buyCard(Copper.class.getName());
        turns.get(0).endTurn();
        System.out.println("there");
    }



}
