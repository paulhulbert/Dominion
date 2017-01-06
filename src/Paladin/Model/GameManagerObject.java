package Paladin.Model;

import Paladin.Controller.UserRequester;
import Paladin.Model.CardTypes.*;
import Paladin.Model.Exceptions.GameLogicException;
import Paladin.View.UIInterface;
import Paladin.View.WaitingScreenLauncher;

import javax.swing.*;
import java.awt.*;
import java.util.*;

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
    public static UIInterface waitingScreen;

    public static Queue<String> playerJoins = new ArrayDeque<>();

    public static boolean localIsHost;
    public static Player localPlayer;
    public static Player currentPlayer;
    public static ArrayList<Player> players = new ArrayList<>();

    public static HashMap<String, SupplyPile> piles = new HashMap<>();

    public static ArrayList<Turn> turns = new ArrayList<>();

    public static ArrayList<Card> trash = new ArrayList<>();

    public static boolean started = false;

    static {
        DatabaseManager.startup();
    }

    public static void joinGame(int ID) throws GameLogicException {
        gameID = ID;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        seed = MessageHandler.seedMap.get(ID);
        setupGame(false);
    }


    public static void setupGame(boolean isHost) throws GameLogicException {
        String playerName = JOptionPane.showInputDialog("Enter a username:");
        Constants.currentCardID = 0;
        //localPlayer = new Player("Paul");
        localIsHost = isHost;
        if (isHost) {
            DatabaseManager.emptyTable();
            seed = System.currentTimeMillis();

            gameID = seededRandom.nextInt(100000);

            String details = "\"Details\":{ \"type\":\"createGame\", \"seed\":\"" + seed + "\" }";
            Message newMessage = new Message(System.currentTimeMillis(),
                    GameManagerObject.gameID, playerName, details);
            DatabaseManager.sendMessage(newMessage);


        }
        seededRandom = new Random(seed);
        players.clear();
        //players.add(localPlayer);

        piles.clear();

        addPile(Copper.class.getName());
        addPile(Silver.class.getName());
        addPile(Gold.class.getName());


        addPile(Curse.class.getName());
        addPile(Estate.class.getName());
        addPile(Duchy.class.getName());
        addPile(Province.class.getName());


        /*   Base Set
        addPile(Village.class.getName(), 10);
        addPile(Woodcutter.class.getName(), 10);
        addPile(Smithy.class.getName(), 10);
        addPile(Cellar.class.getName(), 10);
        addPile(Moat.class.getName(), 10);
        addPile(Workshop.class.getName(), 10);
        addPile(Chancellor.class.getName(), 10);
        addPile(Remodel.class.getName(), 10);
        addPile(Market.class.getName(), 10);
        addPile(Militia.class.getName(), 10);
        addPile(Mine.class.getName(), 10);
        addPile(Festival.class.getName(), 10);
        addPile(Adventurer.class.getName(), 10);
        addPile(Bureaucrat.class.getName(), 10);
        addPile(CouncilRoom.class.getName(), 10);
        addPile(Feast.class.getName(), 10);
        addPile(Gardens.class.getName(), 10);
        addPile(Laboratory.class.getName(), 10);
        addPile(Library.class.getName(), 10);
        addPile(ThroneRoom.class.getName(), 10);
        addPile(Moneylender.class.getName(), 10);
        addPile(Witch.class.getName(), 10);
        addPile(Thief.class.getName(), 10);
        addPile(Spy.class.getName(), 10);
        */


        /*  Hinterlands - tested


         */


        addPile(Crossroads.class.getName());
        addPile(Duchess.class.getName());
        addPile(FoolsGold.class.getName());
        addPile(Develop.class.getName());
        addPile(Oasis.class.getName());
        addPile(Oracle.class.getName());
        addPile(Scheme.class.getName());
        addPile(Tunnel.class.getName());




        turns.clear();



        System.out.println("Game is set up.");

        waitToStartGame(playerName);
    }


    public static void waitToStartGame(String playerName) throws GameLogicException {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Thread thread = new Thread(new WaitingScreenLauncher(), "Waiting screen thread");
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        playerJoins = new ArrayDeque<>();

        String details = "\"Details\":{ \"type\":\"playerJoin\" }";
        Message newMessage = new Message(System.currentTimeMillis(),
                GameManagerObject.gameID, playerName, details);
        DatabaseManager.sendMessage(newMessage);

        while (!started) {
            if (MessageHandler.getPlayerJoins().isEmpty()) {
                try {

                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                playerJoins.add(MessageHandler.getPlayerJoins().remove());
            }
            waitingScreen.update();
        }

        for (String name : playerJoins) {
            Player player = new Player(name);
            if (name.equals(playerName)) {
                localPlayer = player;
            }
            players.add(player);
        }


        generatePiles();


        startGame();

    }

    public static void generatePiles() {

        if (true) {
            return;  //TODO remove this for release
        }

        Collections.shuffle(Constants.baseSetNames, seededRandom);
        for (int i = 0; i < 10; i++) {
            addPile(Constants.baseSetNames.get(i));
        }
    }

    public static void startGame() throws GameLogicException {
        currentPlayer = players.get(0);
        Turn turn = new Turn(currentPlayer);
        turns.add(turn);
        uiInterface.update();

        turn.playTurn();
    }

    public static void trashCard(Player player, Card card) {
        trash.add(card);
    }


    public static void addPile(String name) {
        piles.put(name, new SupplyPile(name));
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static ArrayList<Player> getPlayersAsideFromSpecifiedInOrder(Player player) {
        ArrayList<Player> playerList = new ArrayList<>();
        int playerIndex = players.indexOf(player);

        for (int i = playerIndex + 1; i < players.size(); i++) {
            playerList.add(players.get(i));
        }

        for (int i = 0; i < playerIndex; i++) {
            playerList.add(players.get(i));
        }

        return playerList;
    }


    public static Turn getCurrentTurn() {
        return turns.get(turns.size()-1);
    }

    public static void endturn() throws GameLogicException {
        if (gameOver) {
            return;
        }
        if (checkGameOverConditions()) {
            gameOver = true;

            int highest = -99;
            Player highestPlayer = null;

            String scores = "";

            for (Player player : players) {
                scores += player.getName() + ": " + getScore(player) + "  -  ";
                if (getScore(player) > highest) {
                    highest = getScore(player);
                    highestPlayer = player;
                }
            }
            JOptionPane.showMessageDialog(null, "Game over.  " + highestPlayer.getName() + " wins!" +
                        "            " + scores);

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

    public static int getScore(Player player) {
        int total = 0;

        for (Card card : player.getHand().getCards()) {
            total += card.getVictoryPointWorth(player);
        }
        for (Card card : player.getDeck().getDiscardPile()) {
            total += card.getVictoryPointWorth(player);
        }
        for (Card card : player.getDeck().getDrawPile()) {
            total += card.getVictoryPointWorth(player);
        }
        return total;

    }

    public static boolean checkGameOverConditions() {
        if (piles.get("Paladin.Model.CardTypes.Province").getSize() == 0) {
            return true;
        }

        int supplyPilesEmpty = 0;
        for (String key : piles.keySet()) {
            if (piles.get(key).getSize() == 0 && piles.get(key).inSupply) {
                supplyPilesEmpty++;
            }
        }

        if (supplyPilesEmpty >= 3) {
            return true;
        }

        return false;
    }

    public static void addMessage(Message message) throws GameLogicException {
        MessageHandler.handleMessage(message);
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
