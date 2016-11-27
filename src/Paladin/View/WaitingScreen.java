package Paladin.View;

import Paladin.Model.DatabaseManager;
import Paladin.Model.GameManagerObject;
import Paladin.Model.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaitingScreen extends JDialog implements UIInterface {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea playerListTextArea;

    public WaitingScreen() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Game ID: " + GameManagerObject.gameID);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        if (GameManagerObject.localIsHost) {
            String details = "\"Details\":{ \"type\":\"startTheGameAlready\" }";
            Message newMessage = new Message(System.currentTimeMillis(),
                    GameManagerObject.gameID, "14", details);
            DatabaseManager.sendMessage(newMessage);
        }
        dispose();
    }

    @Override
    public void update() {
        if (GameManagerObject.started || GameManagerObject.localIsHost) {
            buttonOK.setEnabled(true);

            if (!GameManagerObject.localIsHost) {
                onOK();
            }
        }


        String playerList = "";

        for (String name : GameManagerObject.playerJoins) {
            playerList += name + "\n";
        }

        playerListTextArea.setText(playerList);
    }

    public static void main(String[] args) {
        WaitingScreen dialog = new WaitingScreen();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
