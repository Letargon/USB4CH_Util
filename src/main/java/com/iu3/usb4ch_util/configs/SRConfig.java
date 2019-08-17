package com.iu3.usb4ch_util.configs;

import com.iu3.usb4ch_util.configs.model.SRIniGroup;
import com.iu3.usb4ch_util.configs.parser.IniFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andalon
 */
public class SRConfig {

    private final String iniDir;
    private SRIniGroup blast;
    private SRIniGroup pak2bin;
    private SRIniGroup interp;
    private SRIniGroup bin2asc;
    private SRIniGroup binGen;
    private SRIniGroup calibrate;
    private SRIniGroup scopeDisplay;
    private SRIniGroup asciiViewFw;
    private SRIniGroup pakFwrite;
    private SRIniGroup binFwrite;
    private SRIniGroup binFread;
    private SRIniGroup asciiDisplay;

    private List<SRIniGroup> groups;

    private void setDefaults() {
        blast.groupName = "Blast";
        blast.properties.put("DriverName", toSRString("SrUsbXch0"));
        blast.properties.put("SampleRate", toSRString("s11"));
        blast.properties.put("PakFileSize", toSRString("None"));
        blast.properties.put("MaxPackets", "0");
        blast.properties.put("GpsName", toSRString("g2"));
        blast.properties.put("UserConfigByte", toSRString("0x00"));
        blast.properties.put("LedOnDigOut", "1");
        blast.properties.put("MsgCount", "0");

        pak2bin.groupName = "Pak2Bin";
        pak2bin.properties.put("OutputCfgFile", "Off");
        pak2bin.properties.put("OutputStatusFile", "Off");
        pak2bin.properties.put("OutputNmeaFile", "Off");
        pak2bin.properties.put("OutputEquipFile", "Off");
        pak2bin.properties.put("Verbose", "4");
        pak2bin.properties.put("MsgCount", "0");

        bin2asc.groupName = "Bin2Asc";
        bin2asc.properties.put("DataFileMode", toSRString("Single"));
        bin2asc.properties.put("OutputFormat", toSRString("fw4"));
        bin2asc.properties.put("OutputPrefix", toSRString("Counts"));
        bin2asc.properties.put("OutputExt", toSRString("fw4"));
        bin2asc.properties.put("MaxLines", "0");
        bin2asc.properties.put("MsgCount", "0");
        for (int i = 0; i < 4; i++) {
            bin2asc.properties.put("Title" + i, toSRString("Channel " + i));
            bin2asc.properties.put("Places" + i, "3");
            bin2asc.properties.put("Digits" + i, "6");
        }

        calibrate.groupName = "Calibrate";
        for (int i = 0; i < 4; i++) {
            calibrate.properties.put("Units" + i, toSRString("Counts"));
            calibrate.properties.put("Slope" + i, "1.000000e+000");
            calibrate.properties.put("Offset" + i, "0.000000e+000");
        }

        binGen.groupName = "BinGen";
        binGen.properties.put("Npts", "0");
        binGen.properties.put("SamplePeriod", "25000");
        binGen.properties.put("UseTruePeriod", "On");
        binGen.properties.put("MsgCount", "0");

        interp.groupName = "Interp";
        interp.properties.put("OutputRate", "10");
        interp.properties.put("MsgCount", "0");

        scopeDisplay.groupName = "ScopeDisplay";
        scopeDisplay.properties.put("Color", toSRString("Green"));
        scopeDisplay.properties.put("Nbits", "24");
        scopeDisplay.properties.put("AutoDC", "Off");
        scopeDisplay.properties.put("Clipping", "On");
        scopeDisplay.properties.put("FirstSample", "0");
        scopeDisplay.properties.put("FirstTrace", "0");
        scopeDisplay.properties.put("Ndisplay", "8");

        for (int i = 0; i < 4; i++) {
            scopeDisplay.properties.put("Title" + i, toSRString("Channel " + i));
            scopeDisplay.properties.put("Plot" + i, "On");
        }
        for (int i = 4; i < 8; i++) {
            scopeDisplay.properties.put("Title" + i, toSRString("Channel " + i));
            scopeDisplay.properties.put("Plot" + i, "Off");
        }
        for (int i = 8; i < 12; i++) {
            scopeDisplay.properties.put("Title" + i, toSRString("Dig " + (i - 8)));
            scopeDisplay.properties.put("Plot" + i, "On");
        }
        scopeDisplay.properties.put("Title" + 12, toSRString("Toggle"));
        scopeDisplay.properties.put("Plot" + 12, "On");

        scopeDisplay.properties.put("Title" + 13, toSRString("Deg F"));
        scopeDisplay.properties.put("Plot" + 13, "On");

        scopeDisplay.properties.put("Title" + 14, toSRString("NumSat"));
        scopeDisplay.properties.put("Plot" + 14, "On");

        scopeDisplay.properties.put("Title" + 15, toSRString("Power"));
        scopeDisplay.properties.put("Plot" + 15, "On");

        scopeDisplay.properties.put("Title" + 16, toSRString("Trigger"));
        scopeDisplay.properties.put("Plot" + 16, "On");

        asciiViewFw.groupName = "AsciiViewFw";
        asciiViewFw.properties.put("DataFileMode", toSRString("Single"));
        asciiViewFw.properties.put("DataFilePrefix", toSRString("Volts"));
        asciiViewFw.properties.put("DataFileSuffix", toSRString("asc"));
        asciiViewFw.properties.put("DataFileSeqNum", "0");
        asciiViewFw.properties.put("TitleBarText", toSRString("USB4CH_Util"));
        asciiViewFw.properties.put("Color", toSRString("Green"));
        asciiViewFw.properties.put("Clipping", "On");
        asciiViewFw.properties.put("AutoDC ", "Off");
        asciiViewFw.properties.put("FirstSample", "0");
        asciiViewFw.properties.put("FirstTrace", "0");
        asciiViewFw.properties.put("Ndisplay", "4");
        asciiViewFw.properties.put("Nbits", "12");

        for (int i = 0; i < 4; i++) {

            asciiViewFw.properties.put("Plot" + i, "On");
        }
        for (int i = 4; i < 8; i++) {

            asciiViewFw.properties.put("Plot" + i, "Off");
        }
        for (int i = 8; i < 12; i++) {

            asciiViewFw.properties.put("Plot" + i, "On");
        }

        asciiViewFw.properties.put("Plot" + 12, "On");
        asciiViewFw.properties.put("Plot" + 13, "On");
        asciiViewFw.properties.put("Plot" + 14, "On");
        asciiViewFw.properties.put("Plot" + 15, "On");
        asciiViewFw.properties.put("Plot" + 16, "On");
        
        pakFwrite.groupName = "PakFwrite";
        pakFwrite.properties.put("OutputPrefix", toSRString("raw"));
        pakFwrite.properties.put("OutputExt", toSRString("pak"));
        pakFwrite.properties.put("MaxPackets", "0");
        pakFwrite.properties.put("MsgCount", "0");
        
        binFwrite.groupName = "BinFwrite";
        binFwrite.properties.put("OutputPrefix", toSRString("data"));
        binFwrite.properties.put("OutputExt", toSRString("bin"));
        binFwrite.properties.put("MaxBins", "0");
        binFwrite.properties.put("MsgCount", "0");
        
        binFread.groupName = "BinFread";
        binFread.properties.put("Sequential", "Off");
        binFread.properties.put("InputPrefix", toSRString("data"));
        binFread.properties.put("InputExt", toSRString("bin"));
        binFread.properties.put("FirstSeqNum", "0");
        binFread.properties.put("UseTruePeriod", "On");
        binFread.properties.put("WaitUsec", "0");
        binFread.properties.put("MsgCount", "0");
        
        asciiDisplay.groupName = "AsciiDisplay";
        asciiDisplay.properties.put("OutputChannels", "4");
        asciiDisplay.properties.put("RepeatTitle", "25");
        asciiDisplay.properties.put("HexFormat", "Off");
        asciiDisplay.properties.put("MaxLines", "0");
        

    }

    public SRConfig(String ini) {

        this.iniDir = ini;

        groups = new ArrayList<>();

        blast = new SRIniGroup();
        pak2bin = new SRIniGroup();
        interp = new SRIniGroup();
        bin2asc = new SRIniGroup();
        binGen = new SRIniGroup();
        calibrate = new SRIniGroup();
        scopeDisplay = new SRIniGroup();
        asciiViewFw = new SRIniGroup();
        pakFwrite = new SRIniGroup();
        binFwrite = new SRIniGroup();
        binFread = new SRIniGroup();
        asciiDisplay = new SRIniGroup();
        
        groups.add(blast);
        groups.add(pak2bin);
        groups.add(interp);
        groups.add(bin2asc);
        groups.add(binGen);
        groups.add(calibrate);
        groups.add(scopeDisplay);
        groups.add(asciiViewFw);
        groups.add(pakFwrite);
        groups.add(binFwrite);
        groups.add(binFread);
        groups.add(asciiDisplay);
        
        setDefaults();
        try {

            File f = new File(ini);
            BufferedReader br = null;
            if (f.exists() && !f.isDirectory()) {
                br = new BufferedReader(new FileReader(ini));
            }

            if (br == null || br.readLine() == null) {
                loadDefaults();
                setValue("USB4CH_Util", "StationName", "MAGD");
            }
        } catch (IOException ex) {
            Logger.getLogger(SRConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String toSRString(String str) {
        return "\"" + str + "\"";
    }

    public String getValue(String groupName, String key) {

        HashSet<SRIniGroup> iniGroups = new IniFile(iniDir).read();

        for (SRIniGroup group : iniGroups) {
            if (group.groupName.equals(groupName)) {
                return group.properties.get(key);
            }
        }
        return null;
    }
    static public String getValue(String path, String groupName, String key) {

        HashSet<SRIniGroup> iniGroups = new IniFile(path).read();

        for (SRIniGroup group : iniGroups) {
            if (group.groupName.equals(groupName)) {
                return group.properties.get(key);
            }
        }
        return null;
    }

    public final boolean setValue(String groupName, String property, String value) {
        SRIniGroup data = new SRIniGroup();
        data.groupName = groupName;
        data.properties.put(property, value);

        return new IniFile(iniDir).write(data);
    }

    public final boolean loadDefaults() {

        boolean success = true;

        for (SRIniGroup group : groups) {
            success = success && new IniFile(iniDir).write(group);
        }
        return success;
    }

    public boolean setGroup(SRIniGroup data) {

        return new IniFile(iniDir).write(data);
    }

}
