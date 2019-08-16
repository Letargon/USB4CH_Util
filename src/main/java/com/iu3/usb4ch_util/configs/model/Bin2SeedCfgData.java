/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.configs.model;

/**
 * The SR Bin2Seed.cfg configuration file contains parameters which can be
 * edited to fit your situation. However, the format of this file must remain
 * unchanged so Bin2Seed can still read it. This means you should not add or
 * remove lines and you should only change text after the ; or = characters. The
 * available parameters are NCHANNELS, NAMESTYLE, WAITFORGOODTIME, and CHANNEL
 * #.
 *
 * NCHANNELS can be from 1 to 8 and selects how many SEED files are output.
 * Typical values would be 4 for a USB4CH and 8 for a USB8CH. The channels are
 * output sequentially starting from channel 0. So if NCHANNELS is 2, then data
 * for channels 0 and 1 will be output. * NAMESTYLE can be either SDS for the
 * SeisComP Data Structure layout or BUD for the Buffer of Uniform Data layout.
 * The directories and files are organized as follows for:
 *
 * SDS -> YEAR/NET/STA/CHAN.TYPE/NET.STA.LOC.CHAN.TYPE.YEAR.DAY BUD ->
 * NET/STA/STA.NET.LOC.CHAN.YEAR.DAY
 *
 * where YEAR is the four digit current year, DAY is the three digit Julian day,
 * NET is the two character network name (eg SR), STA is the five or less
 * character station name (eg SRHQ), LOC is the two character sensor location
 * code (eg C0), CHAN is the three character channel component (eg HNZ), and
 * TYPE is a channel type code (eg D for data). * The directories and files are
 * created under the Examples\SeedData or Examples\SeedData\YMDHMS directory
 * created by the BlastToSeed.bat pipeline control batch file. The YMDHMS part
 * is selected by editing the batch file to move the comment symbol between the
 * two MAKE_YMDHMS= lines. When sending data to SeisComP, just writing to the
 * SeedData directory is easiest. Just be sure the name of your SeedData
 * directory has NO spaces.
 *
 * WAITFORGOODTIME can be either YES or NO. It controls whether Bin2Seed starts
 * outputting data right away or whether it waits until a valid time has been
 * identified. This is important because the time is written once at the start
 * of the SEED file. When using timing mode G2 for GPS time, it can take at
 * least 2 seconds to get a good time value. Choose YES so no data files are
 * written until then. YES is also appropriate for timing mode G5 since it has a
 * valid time right away. But timing mode G0 starts from time 0, so choose NO to
 * allow ignoring this check.
 *
 * CHANNEL # supplies the NET STA LOC CHAN values for the USBxCH input number #.
 * The various channel identification parts should be separated by white space,
 * be given in this exact order, and none should be longer than the allowed
 * lengths of 2 5 2 3 characters respectively. Of course, downstream processes
 * will not work correctly unless these id fields exactly match what the
 * downstream processing expects.
 *
 * @author User
 */
public class Bin2SeedCfgData {

    private String nchannels;
    private String namestyle;
    private String waitforgoodtime;
    private final String[] channels = new String[8];

    public Bin2SeedCfgData() {
        this.nchannels = "4";
        this.namestyle = "BUD";
        this.waitforgoodtime = "YES";

        for (int i = 0; i < 8; i++) {
            channels[0] = "MG HQ C" + i + " HNZ";
        }
    }

    public Bin2SeedCfgData(String nchannels, String namestyle, String waitforgoodtime) {
        this.nchannels = nchannels;
        this.namestyle = namestyle;
        this.waitforgoodtime = waitforgoodtime;
    }

    public void setNchannels(String nchannels) {
        this.nchannels = nchannels;
    }

    public void setNamestyle(String namestyle) {
        this.namestyle = namestyle;
    }

    public void setWaitforgoodtime(String waitforgoodtime) {
        this.waitforgoodtime = waitforgoodtime;
    }

    public void setChannel(int ind, String cname) {
        this.channels[ind] = cname;
    }

    public String getNchannels() {
        return nchannels;
    }

    public String getNamestyle() {
        return namestyle;
    }

    public String getWaitforgoodtime() {
        return waitforgoodtime;
    }

    public String getChannel(int ind) {
        return channels[ind];
    }

}
