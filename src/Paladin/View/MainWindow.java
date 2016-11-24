package Paladin.View;

import Paladin.Controller.DesktopUserRequester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import javax.swing.*;
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
                try {
                    GameManagerObject.setupGame(true);
                    GameManagerObject.startGame();
                } catch (GameLogicException e1) {
                    e1.printStackTrace();
                }

                update();
            }
        });
        joinGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int gameID = Integer.parseInt(JOptionPane.showInputDialog("Enter game ID:"));
                    GameManagerObject.setupGame(false);
                    GameManagerObject.startGame();
                } catch (GameLogicException e1) {
                    e1.printStackTrace();
                }

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
    private JTextArea currentTurn;
    private JTextArea currentStatus;
    private JPanel mainPanel;
    private JButton newGameButton;
    private JButton joinGameButton;


    public void update() {
        debugOutput();
        Player localPlayer = GameManagerObject.localPlayer;
        Hand localHand = localPlayer.getHand();

        ((DefaultListModel)myHand.getModel()).clear();

        for (Card card : localHand.getCards()) {
            ((DefaultListModel)myHand.getModel()).addElement(card.getName());
        }

        ((DefaultListModel)decks.getModel()).clear();

        for (String s : GameManagerObject.piles.keySet()) {
            SupplyPile pile = GameManagerObject.piles.get(s);
            String nameOfPile = pile.getName();
            String costOfPile = "~";
            if (pile.getTopCard() != null) {
                costOfPile = pile.getTopCard().getCost() + "";
            }
            String cardsRemaining = pile.getSize() + "";
            ((DefaultListModel)decks.getModel()).addElement(nameOfPile + " - Cost: " + costOfPile + " - Remaining: " + cardsRemaining);
        }

        Turn currentTurn = GameManagerObject.turns.get(GameManagerObject.turns.size()-1);
        currentStatus.setText("Actions: " + currentTurn.getCurrentActions() + "\n" +
                                "Money: " + currentTurn.getCurrentMoney() + "\n" +
                                "Buys: " + currentTurn.getCurrentBuys() + "\n" +
                                "Draw Pile: " + localPlayer.getDeck().getDrawSize() + "\n" +
                                "Discard: " + localPlayer.getDeck().getDiscardSize());


        String cardsPlayed = "";

        for (Card card : currentTurn.getCardsPlayedThisTurn()) {
            cardsPlayed += card.getName() + "\n";
        }

        this.currentTurn.setText(cardsPlayed);
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
