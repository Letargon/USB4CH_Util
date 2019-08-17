package com.iu3.usb4ch_util.pipeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andalon
 */
public class DeviceChecker implements Runnable {

    volatile boolean isOn;
    volatile boolean isActive;
    String presence;
    AnchorPane statusPane;

    @Override
    public void run() {
        try {
            while (isActive) {
                boolean errFound = false;
                Process proc = new ProcessBuilder(presence).start();
                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.contains("Err 26")) {
                        isOn = false;
                        errFound = true;
                        statusPane.setStyle("-fx-background-color:rgb(255,0,0)");
                    }
                }
                if (!errFound) {
                    isOn = true;
                    statusPane.setStyle("-fx-background-color:rgb(0,255,0)");
                }
                Thread.sleep(1000);

            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(DeviceChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public synchronized boolean getStatus(){
        return isOn;
    }
    public synchronized void turnOff(){
        isActive = false;
    }
    public DeviceChecker(AnchorPane statusPane) {
        this.statusPane = statusPane;
        presence = PipelineLauncher.EXE_DIR + "/Presence.exe";
        isActive = true;
    }
}
