/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andalon
 */
public class ProcMonitor implements Runnable {

    private final PipelineLauncher ppLauncher;
    private List<Process> procList;
    private volatile boolean running;
    private volatile boolean errorMark = false;


    public ProcMonitor(PipelineLauncher ppLauncher) {
        this.ppLauncher = ppLauncher;
        this.procList = new ArrayList<>();
    }
    public void setProcList(List<Process> procList){
        this.procList = procList;
    }
    @Override
    public void run() {
        running = true;
        while (running) {
            for (Process p : procList) {
                if (!p.isAlive()) {
                    errorMark = true;
                    ppLauncher.terminate();
                    running = false;
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (!running) {
            System.out.println("Monitor terminated");
        } 
        if (!errorMark) {
            System.out.println("Error: Pipeline terminated!!");
        }
        

    }

    public synchronized void stop() {

        running = false;

    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized boolean isErrorMark() {
        return errorMark;
    }
    
    
}
