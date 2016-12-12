package Paladin.View;

import Paladin.Controller.DesktopUserRequester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by paulh on 11/19/2016.
 */
public class MainWindow implements UIInterface{
    private JList<String> myHand;

    public MainWindow() {
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread gameThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GameManagerObject.setupGame(true);
                        } catch (GameLogicException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, "Game engine thread");

                gameThread.start();


                update();
            }
        });
        joinGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread gameThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            int gameID = Integer.parseInt(JOptionPane.showInputDialog("Enter game ID:"));
                            GameManagerObject.joinGame(gameID);
                        } catch (GameLogicException e1) {
                            e1.printStackTrace();
                        }

                    }
                }, "Game engine thread");

                gameThread.start();

                update();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        MainWindow mainWindow = new MainWindow();
        GameManagerObject.uiInterface = mainWindow;
        GameManagerObject.userRequester = new DesktopUserRequester();
        frame.setContentPane(mainWindow.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JList<String> decks;
    private JTextArea currentTurnText;
    private JTextArea currentStatus;
    private JPanel mainPanel;
    private JButton newGameButton;
    private JButton joinGameButton;


    public void update() {
        //debugOutput();
        Player localPlayer = GameManagerObject.localPlayer;
        if (localPlayer == null || localPlayer.getHand() == null) {
            return;
        }
        Hand localHand = localPlayer.getHand();




        ArrayList<String> pilesStrings = new ArrayList<>();

        for (String s : GameManagerObject.piles.keySet()) {
            SupplyPile pile = GameManagerObject.piles.get(s);
            String nameOfPile = pile.getName();
            String costOfPile = "~";
            if (pile.getTopCard() != null) {
                costOfPile = pile.getTopCard().getCost() + "";
            }
            String cardsRemaining = pile.getSize() + "";
            pilesStrings.add(nameOfPile + " - Cost: " + costOfPile + " - Remaining: " + cardsRemaining);

        }

        Turn currentTurn = GameManagerObject.turns.get(GameManagerObject.turns.size()-1);

        String cardsPlayed = "";

        for (Card card : currentTurn.getCardsPlayedThisTurn()) {
            cardsPlayed += card.getName() + "\n";
        }

        final String cardsPlayedFinal = cardsPlayed;


        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ((DefaultListModel)myHand.getModel()).clear();

                    for (Card card : localHand.getCards()) {
                        ((DefaultListModel)myHand.getModel()).addElement(card.getName());
                    }




                    ((DefaultListModel)decks.getModel()).clear();

                    for (String s : pilesStrings) {
                        ((DefaultListModel)decks.getModel()).addElement(s);
                    }

                    currentStatus.setText("Actions: " + currentTurn.getCurrentActions() + "\n" +
                            "Money: " + currentTurn.getCurrentMoney() + "\n" +
                            "Buys: " + currentTurn.getCurrentBuys() + "\n" +
                            "Draw Pile: " + localPlayer.getDeck().getDrawSize() + "\n" +
                            "Discard: " + localPlayer.getDeck().getDiscardSize());

                    currentTurnText.setText(cardsPlayedFinal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void debugOutput() {
        ArrayList<Card> cardsInHand = GameManagerObject.localPlayer.getHand().getCards();
        ArrayList<Card> cardsInDraw = GameManagerObject.localPlayer.getDeck().getDrawPile();
        ArrayList<Card> cardsInDiscard = GameManagerObject.localPlayer.getDeck().getDiscardPile();
        ArrayList<Card> cardsInTrash = GameManagerObject.trash;
        ArrayList<Card> cardsPlayedThisTurn = GameManagerObject.getCurrentTurn().getCardsPlayedThisTurn();
        ArrayList<Card> cardsGainedThisTurn = GameManagerObject.getCurrentTurn().getCardsGainedThisTurn();



        System.out.println("Cards in hand: " + cardsInHand);
        System.out.println("Cards in draw: " + cardsInDraw);
        System.out.println("Cards in discard: " + cardsInDiscard);
        System.out.println("Cards in trash: " + cardsInTrash);
        System.out.println("Cards played this turn: " + cardsPlayedThisTurn);
        System.out.println("Cards gained this turn: " + cardsGainedThisTurn);
    }

    private void createUIComponents() {
        myHand = new JList<String>(new DefaultListModel<String>());
        decks = new JList<String>(new DefaultListModel<String>());
    }
}
