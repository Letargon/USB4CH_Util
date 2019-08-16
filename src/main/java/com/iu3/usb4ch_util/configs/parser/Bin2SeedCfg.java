/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.configs.parser;

import com.iu3.usb4ch_util.configs.model.Bin2SeedCfgData;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author User
 */
public class Bin2SeedCfg {
    
    private final String cfgDir;
    
    public Bin2SeedCfg(String cfgDir) {
        this.cfgDir = cfgDir;
    }
    
    public boolean write(Bin2SeedCfgData data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cfgDir, false))) {
            
            writer.write(";");
            writer.newLine();
            writer.write(";");
            writer.newLine();
            writer.write(";");
            writer.newLine();
            
            writer.write(";");
            writer.newLine();
            writer.write(";");
            writer.newLine();
            
            writer.newLine();
            writer.write("NCHANNELS = " + data.getNchannels());
            writer.newLine();
            
            writer.newLine();
            writer.write("NAMESTYLE = " + data.getNamestyle());
            writer.newLine();
            
            writer.newLine();
            writer.write("WAITFORGOODTIME = " + data.getWaitforgoodtime());
            writer.newLine();
            
            writer.newLine();
            for (int i = 0; i < 8; i++) {
                writer.write("CHANNEL  " + i + " = " + data.getChannel(i));
                writer.newLine();
                
            }
            
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    public Bin2SeedCfgData read() {
        
        Bin2SeedCfgData data = new Bin2SeedCfgData();
        try (BufferedReader reader = new BufferedReader(new FileReader(cfgDir))) {
            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();
            data.setNchannels(getRecordData(reader.readLine()));
            reader.readLine();
            data.setNamestyle(getRecordData(reader.readLine()));
            reader.readLine();
            data.setWaitforgoodtime(getRecordData(reader.readLine()));
            reader.readLine();
            for (int i = 0; i < 8; i++) {
                data.setChannel(i, getRecordData(reader.readLine()));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return data;
    }
    
    private String getRecordData(String line) {
        String[] spl = line.split("\\s=\\s");
        return spl[spl.length - 1];
        
    }
    
    public boolean writeStandart(String stationName, boolean waitforgoodtime) {
        Bin2SeedCfgData data = new Bin2SeedCfgData();
        data.setNchannels("4");
        data.setNamestyle("BUD");
        data.setWaitforgoodtime(waitforgoodtime ? "YES" : "NO");
        for (int i = 0; i < 8; i++) {
            data.setChannel(i, "SR " + stationName + " C" + i + " NHZ");
        }
        return write(data);
    }
    
}
