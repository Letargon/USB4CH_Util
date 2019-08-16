/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.pipeline;

import com.iu3.usb4ch_util.configs.SRConfig;
import com.iu3.usb4ch_util.configs.parser.Bin2SeedCfg;
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

    public static String EXE_DIR = "C:\\SR\\USBXCH\\Exe";
    private List<String> iniFileList;
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
        iniFileList = new ArrayList<>();
        srDir = new String();

    }

    public void setSrDir(String srDir) {
        this.srDir = srDir;
    }

    private void setArgsList(boolean nullEnding) {

        argsList.clear();

        for (int i = 0; i < pipeNames.size() - 1; i++) {
            String[] fileName = pipeNames.get(i + 1).split("\\.");
            String[] args = {"\"" + iniFileList.get(i) + "\"", "/to", fileName[0]};
            argsList.add(args);
        }

        String[] args;
        if (nullEnding) {
            args = new String[]{"\"" + iniFileList.get(iniFileList.size() - 1) + "\"", "/to", "NULL"};
        } else {
            args = new String[]{"\"" + iniFileList.get(iniFileList.size() - 1) + "\""};
        }
        argsList.add(args);
    }

    private void setArgAlone() {
        argsList.clear();
        String args[] = {"\"" + iniFileList.get(0) + "\""};
        argsList.add(args);
    }

    public void setTimeScope() {

        pipeNames.clear(); //clear vector
        iniFileList.clear();

        pipeNames.add("Blast.exe");
        iniFileList.add(iniFile);

        pipeNames.add("Pak2Bin.exe");
        iniFileList.add(iniFile);

        pipeNames.add("Interp.exe");
        iniFileList.add(iniFile);

        pipeNames.add("ScopeDisplay.exe");
        iniFileList.add(iniFile);

        pipeNames.add("Bin2Asc.exe");
        iniFileList.add(iniFile);

        setArgsList(true);
    }

    public void setTestConfig() {

        pipeNames.clear();
        iniFileList.clear();

        pipeNames.add("BinGen.exe");
        iniFileList.add(iniFile);

        pipeNames.add("Bin2Asc.exe");
        iniFileList.add(iniFile);

        pipeNames.add("ScopeDisplay.exe");
        iniFileList.add(iniFile);

        pipeNames.add("Bin2Seed.exe");

        Bin2SeedCfg cfgFile = new Bin2SeedCfg("Bin2Seed.cfg");

        String stationName = SRConfig.getValue(iniFile, "USB4CH_Util", "StationName");
        String gpsName = SRConfig.getValue(iniFile, "Blast", "GpsName");
        cfgFile.writeStandart(stationName, gpsName.equals("\"g2\""));

        iniFileList.add(System.getProperty("user.dir") + "/Bin2Seed.cfg");

        setArgsList(false);
    }

    public void setAsciiViewer() {
        pipeNames.clear();
        iniFileList.clear();

        pipeNames.add("AsciiViewFw");
        iniFileList.add(iniFile);

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
                    if (argsList.size() == 3 || i != pipeNames.size() - 1) {
                        pb = new ProcessBuilder(command, argsList.get(i)[0],
                                argsList.get(i)[1], argsList.get(i)[2]);
                    } else {
                        pb = new ProcessBuilder(command, argsList.get(i)[0]);
                    }
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
