/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andalon
 */
public class PipelineLauncher {

    public static final String EXE_DIR = "C:\\SR\\USBXCH\\Exe";
    private String iniFile;

    private List<String> pipeNames;
    private List<String[]> argsList;
    private List<Process> procList;

    private String srDir;

    private ProcMonitor monitor;

    public PipelineLauncher(String iniFile) {
        procList = new CopyOnWriteArrayList<>();
        pipeNames = new ArrayList<>();
        argsList = new ArrayList<>();
        this.iniFile = iniFile;
        srDir = new String();

    }

    public void setSrDir(String srDir) {
        this.srDir = srDir;
    }

    private void setArgsList() {

        argsList.clear();

        for (int i = 0; i < pipeNames.size() - 1; i++) {
            String[] fileName = pipeNames.get(i + 1).split("\\.");
            String[] args = {"\"" + iniFile + "\"", "/to", fileName[0]};
            argsList.add(args);
        }
        String[] args = {"\"" + iniFile + "\"", "/to", "NULL"};
        argsList.add(args);
    }

    private void setArgAlone() {
        argsList.clear();
        String args[] = {"\"" + iniFile + "\""};
        argsList.add(args);
    }

    public void setTimeScope() {

        pipeNames.clear(); //clear vector
        pipeNames.add("Blast.exe");
        pipeNames.add("Pak2Bin.exe");
        pipeNames.add("Interp.exe");
        pipeNames.add("ScopeDisplay.exe");
        pipeNames.add("Bin2Asc.exe");

        setArgsList();
    }

    public void setTestConfig() {
        pipeNames.clear();
        pipeNames.add("BinGen.exe");
        pipeNames.add("Bin2Asc.exe");
        pipeNames.add("ScopeDisplay.exe");
        setArgsList();
    }

    public void setAsciiViewer() {
        pipeNames.clear();
        pipeNames.add("AsciiViewFw");
        setArgAlone();
    }

    public void terminate() {
        while (procList.size() > 0) {
            Process p = procList.get(0);
            p.destroy();
            procList.remove(p);
        }
    }

    public synchronized boolean runPipeLine(boolean pipeline) {
        terminate();
        try {
            for (int i = 0; i < pipeNames.size(); i++) {

                String command = EXE_DIR + "/" + pipeNames.get(i);
                ProcessBuilder pb;
                if (!pipeline) {
                    pb = new ProcessBuilder(command, argsList.get(i)[0]);
                } else {
                    pb = new ProcessBuilder(command, argsList.get(i)[0],
                            argsList.get(i)[1], argsList.get(i)[2]);
                }
                if (!srDir.isEmpty()) {
                    pb.directory(new File(srDir));
                }
                pb.environment().put("PATH", EXE_DIR);
                Process pipeProc = pb.start();

                if (!pipeProc.isAlive()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(pipeProc.getErrorStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                    return false;
                }
                procList.add(pipeProc);
            }

        } catch (IOException ex) {
            Logger.getLogger(PipelineLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }

        runMonitor();
        return true;
    }

    public void runMonitor() {
        monitor = new ProcMonitor(this, procList);
        new Thread(monitor).start();
    }

}
