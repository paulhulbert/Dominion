package Paladin.View;

import Paladin.Controller.DesktopUserRequester;
import Paladin.Model.*;
import Paladin.Model.Exceptions.GameLogicException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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



    public void update() {
        Player localPlayer = GameManagerObject.localPlayer;
        Hand localHand = localPlayer.getHand();

        ((DefaultListModel)myHand.getModel()).clear();

        for (Card card : localHand.getCards()) {
            ((DefaultListModel)myHand.getModel()).addElement(card.getName());
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

    private void createUIComponents() {
        myHand = new JList<String>(new DefaultListModel<String>());
        decks = new JList<String>(new DefaultListModel<String>());
    }
}
