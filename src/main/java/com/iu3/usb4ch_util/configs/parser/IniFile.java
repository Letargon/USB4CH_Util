/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.configs.parser;

import com.iu3.usb4ch_util.configs.model.SRIniGroup;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andalon
 */
public class IniFile {

    private final String iniDir;

    public IniFile(String iniDir) {
        this.iniDir = iniDir;
    }

    public HashSet<SRIniGroup> read() {
        HashSet<SRIniGroup> groups = new HashSet<>();//group + key = uniq and value
        HashMap<String, HashMap<String, String>> gMap = new HashMap<>();
        Scanner reader = null;
        try {
            File file = new File(iniDir);
            if (file.exists() && !file.isDirectory()) {
                reader = new Scanner(file);
                String iniData;
                SRIniGroup groupData;
                String group = new String();
                String key = new String();
                String value = new String();
                boolean isKey = false;
                boolean isValue = false;
                boolean IsDiscr = false;
                int length;
                if (reader.hasNext()) {
                    reader.next();//первая строка предопределена не интересует
                }
                while (reader.hasNext()) {
                    iniData = reader.next();

                    length = iniData.length();

                    if (iniData.charAt(0) == '[' && iniData.charAt(length - 1) == ']') {
                        group = iniData.substring(1, length - 1);
                        isKey = false;
                        isValue = false;
                    } else {
                        isKey = true;
                    }
                    if (isValue) {
                        if (key.equals("Title1")) {
                            int k = 5 + 1;
                        }

                        if (iniData.charAt(0) == '\"') {
                            IsDiscr = true;
                        }
                        if (iniData.charAt(iniData.length() - 1) == '\"' && (iniData.length() > 1 || iniData.contains(" "))) {

                            IsDiscr = false;
                        }
                        value += " " + iniData;
                        isKey = false;
                    }
                    if (isValue && !IsDiscr) {

                        HashMap<String, String> props = gMap.get(group);
                        if (props == null) {
                            props = new HashMap<>();
                        }

                        props.put(key, value);
                        gMap.put(group, props);
                        value = "";
                        isKey = false;

                        isValue = false;
                    }

                    if (iniData.equals("=")) {
                        isKey = false;
                        isValue = true;
                    }
                    if (!group.isEmpty() && isKey) {
                        key = iniData;
                    }

                }
                for (Entry<String, HashMap<String, String>> entry : gMap.entrySet()) {
                    groups.add(new SRIniGroup(entry.getKey(), entry.getValue()));
                }
                reader.close();
                return groups;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IniFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean writeSRIni(SRIniGroup mgroupData) {

        HashSet<SRIniGroup> groups = read();
        //add group data to ini map
        if (groups == null) {
            groups = new HashSet<>();
        }
        SRIniGroup old;

        if (!groups.add(mgroupData)) {
            for (SRIniGroup gr : groups) {
                if (gr.equals(mgroupData)) {
                    old = gr;
                    old.properties.putAll(mgroupData.properties);
                }
            }
        };

        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(iniDir));
            writer.write("SR_USBXCH_Ini\r\n");//первая строка sr_ini файла

            for (SRIniGroup groupData : groups) {

                writer.write("[" + groupData.groupName + "]\r\n");

                for (Entry<String, String> entry : groupData.properties.entrySet()) {
                    writer.write(entry.getKey() + " = " + entry.getValue() + "\r\n");
                }
            }

            writer.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(IniFile.class.getName()).log(Level.SEVERE, null, ex);

        }
        return false;
    }
}
