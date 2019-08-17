package com.iu3.usb4ch_util.pipeline.time_manager;

import com.iu3.usb4ch_util.configs.SRConfig;
import com.iu3.usb4ch_util.model.FileName;
import com.iu3.usb4ch_util.pipeline.PipelineLauncher;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class TimeManager implements Runnable {

    private final PipelineLauncher pll;
    private final SRConfig conf;
    private final String stationName;

    private volatile boolean running;

    public TimeManager(PipelineLauncher pll, SRConfig conf, String stationName) {
        this.pll = pll;
        this.conf = conf;
        this.stationName = stationName;
        this.running = false;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            
            setupConf();
            pll.runPipeLine(true);

            long relaunchTime = getMSecTillMidnight();
            try {
                Thread.sleep(relaunchTime);
                if (!pll.isAlife()) {
                    running = false;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(TimeManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private synchronized void stop() {
        running = false;
    }

    private synchronized void setupConf() {

        String fileName = FileName.getFileName(stationName);
        fileName = fileName.replaceAll("\\s+", "");
        conf.setValue("Bin2Asc", "OutputPrefix", SRConfig.toSRString(fileName));
        conf.setValue("PakFwrite", "OutputPrefix", SRConfig.toSRString(fileName));
        conf.setValue("BinFwrite", "OutputPrefix", SRConfig.toSRString(fileName));
        
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));
        String srDir = pll.getSrDirRoot() + "/" + zdt.getYear() + "/" + zdt.getMonth().toString() + "/";
        int day = zdt.getDayOfMonth();
        String decade = (day < 11 ? "FIRST" : (day < 21 ? "SECOND" : "THIRD"));
        srDir += decade + "/" + stationName.trim()+"/";
        
        File dir = new File(srDir);
        if (!dir.exists()){
            dir.mkdirs();
        }
        pll.setSrDir(dir.getAbsolutePath());

    }

    private synchronized long getMSecTillMidnight() {

        String timeZone = "UTC";

        long midnightUtc = LocalDate.now().plusDays(1).atStartOfDay().atZone(ZoneId.of(timeZone)).toInstant().toEpochMilli();

        return midnightUtc - ZonedDateTime.now(ZoneId.of(timeZone)).toInstant().toEpochMilli();
    }
}
