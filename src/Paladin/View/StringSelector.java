package Paladin.View;

import Paladin.Model.Card;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StringSelector extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JComboBox optionBox;
    private JTextArea messageLabel;

    private ArrayList<String> returnValue;

    public StringSelector(ArrayList<String> options, String message, String title) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle(title);
        messageLabel.setText(message);
        setLocation(700,300);

        for (String string : options) {
            optionBox.addItem(string);
        }

        returnValue = options;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        String selected = (String) optionBox.getSelectedItem();
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
