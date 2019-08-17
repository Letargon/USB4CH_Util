/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * @author Andalon
 */
public class FileName {

    private final String station;
    private String time;

    public FileName(String station) {
        this.station = station;
    }

    public String getFileName() {

        return station + getTime();

    }

    static public String getFileName(String station) {

        return station + getTime();

    }

    static public String getTime() {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));

        String year = Integer.toString(zdt.getYear());

        String month = getString2(zdt.getMonthValue());

        String day = getString2(zdt.getDayOfMonth());

        String hour = getString2(zdt.getHour());

        String minute = getString2(zdt.getMinute());

        String sec = getString2(zdt.getSecond());

        return year + month + day + hour + minute + sec;
    }

    static private String getString2(Integer num) {
        return num > 9 ? num.toString() : "0" + num.toString();
    }

}
