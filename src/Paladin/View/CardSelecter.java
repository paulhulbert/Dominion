package Paladin.View;

import Paladin.Model.Card;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CardSelecter extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel mainPanel;
    private JPanel insidePanel;
    private JComboBox optionBox;

    private ArrayList<Card> returnValue;

    public CardSelecter(ArrayList<Card> options) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        for (Card card : options) {
            optionBox.addItem(card);
        }

        returnValue = options;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        Card selected = (Card) optionBox.getSelectedItem();
        returnValue.add(selected);
        dispose();
    }

    public static void main(String[] args) {
        CardSelecter dialog = new CardSelecter(new ArrayList<Card>());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
