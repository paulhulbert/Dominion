package Paladin.View;

import Paladin.Model.Card;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CardSelector extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JComboBox optionBox;
    private JTextArea messageLabel;

    private ArrayList<Card> returnValue;

    public CardSelector(ArrayList<Card> options, String message, String title) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle(title);
        messageLabel.setText(message);
        setLocation(700,300);

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
        CardSelector dialog = new CardSelector(new ArrayList<Card>(), "message", "title");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
