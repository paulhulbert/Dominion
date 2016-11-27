package Paladin.View;

import Paladin.Model.GameManagerObject;

/**
 * Created by paulh on 11/23/2016.
 */
public class WaitingScreenLauncher implements Runnable {
    @Override
    public void run() {
        WaitingScreen dialog = new WaitingScreen();
        GameManagerObject.waitingScreen = dialog;
        dialog.pack();
        dialog.setVisible(true);
    }
}
