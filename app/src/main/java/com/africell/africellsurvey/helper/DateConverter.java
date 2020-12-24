package com.africell.africellsurvey.helper;

import android.text.format.Time;

import androidx.room.TypeConverter;

import java.sql.Date;


public class DateConverter {

    /**
     * Get current time in human-readable form.
     * @return current time as a string.
     */
    public String getNow() {
        Time now = new Time();
        now.setToNow();
        String sTime = now.format("%Y_%m_%d %T");
        return sTime;
    }
    /**
     * Get current time in human-readable form without spaces and special characters.
     * The returned value may be used to compose a file name.
     * @return current time as a string.
     */
    public String getTimeStamp() {
        Time now = new Time();
        now.setToNow();
        String sTime = now.format("%Y_%m_%d_%H_%M_%S");
        return sTime;
    }
}
