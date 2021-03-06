package Paladin.Model;

import Paladin.Model.Exceptions.GameLogicException;
import com.fasterxml.jackson.core.JsonParser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by paulh on 10/1/2016.
 */
public class DatabaseManager {

    public static int gameID = 17;

    public static String playerName = "Paul";

    private static Connection con;

    private static long lastRefreshID = -1;

    private static class Refresher extends Thread {
        public Refresher() {
            super("Database Refresher");
        }

        @Override
        public void run() {
            super.run();

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshDatabase();
            }

            /*Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    refreshDatabase();
                }
            });
            timer.start();*/
        }
    }

    public static void startup() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://204.246.56.27/dominion", "dominion", "La9mT6?92K!G");


            new Refresher().start();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://204.246.56.27/dominion", "dominion", "La9mT6?92K!G");



        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //for (int i = 0; i < 50; i++) {
        //    addRow("details" + i);
        //}

        //for (String s : getNewRows()) {
        //    System.out.println(s);
        //}

        emptyTable();

    }

    public static boolean sendMessage(Message message) {
        try {
            Statement stmt = con.createStatement();
            return stmt.execute("INSERT INTO `dominion`.`actions` " +
                    "(`time_created`, `GameID`, `Player`, `Details`) " +
                    "VALUES ( " + message.time_created + ", " + message.GameID + ", '" + message.Player +
                    "', '" + message.Details + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void emptyTable() {
        try {
            Statement stmt = con.createStatement();
            stmt.execute("DELETE FROM `dominion`.`actions` WHERE ID > -1;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean addRow(String actionJson) {

        try {
            Statement stmt = con.createStatement();
            return stmt.execute("INSERT INTO `dominion`.`actions` " +
                    "(`time_created`, `GameID`, `Player`, `Details`) " +
                    "VALUES ( " + System.nanoTime() + ", " + gameID + ", '" + playerName + "', '" + actionJson + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void refreshDatabase() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM actions WHERE ID > " +
                    lastRefreshID + " AND GameID = " + GameManagerObject.gameID);

            while (rs.next()) {
                long newRecentID = rs.getLong(1);
                lastRefreshID = newRecentID;
                MessageHandler.handleMessage(new Message(rs.getLong(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<String> getNewRows() {

        ArrayList<String> newRows = new ArrayList<>();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM actions WHERE ID > " + lastRefreshID);

            while (rs.next()) {
                long newRecentID = rs.getLong(1);
                String s =  newRecentID + " - " + rs.getLong(2) + " - " +
                        rs.getInt(3) + " - " + rs.getString(4) + " - " + rs.getString(5);
                newRows.add(s);
                lastRefreshID = newRecentID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newRows;
    }
}
