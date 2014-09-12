/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package subeditor;

/**
 *
 * @author Staff
 */
public class Time {
    
    private int hour;
    private int minute;
    private int second;
    private int milliSecond;
    private int allMilliSecond;
    public int index;
    public int end;
    public String srtFormat;
    
    public Time(int hour, int minute, int second, int milliSecond, int index) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.milliSecond = milliSecond;
        this.index = index;
        end = this.index + 12;
        allMilliSecond = (milliSecond) + (second * 1000) + (minute * 60 * 1000) + (hour * 60 * 60 * 1000);
        srtFormat = toSRTFormat();
    }
    
    public void setMilli(int milliSecond) {
        allMilliSecond = milliSecond;
        updateGeneral();
    }
    
    public void addMilli(int milliSecond) {
        allMilliSecond += milliSecond;
        updateGeneral();
    }
    
    public int getMilli() {
        return allMilliSecond;
    }
    
    public void updateGeneral() {
        int remain = allMilliSecond;
        hour = remain / (60 * 60 * 1000);
        remain -= (hour * (60 * 60 * 1000));
        minute = remain / (60 * 1000);
        remain -= (minute * (60 * 1000));
        second = remain / 1000;
        remain -= (second * 1000);
        milliSecond = remain;
        srtFormat = toSRTFormat();
    }
    
    public String toSRTFormat() {
        String hourText = insertZero(2, hour);
        String minuteText = insertZero(2, minute);
        String secondText = insertZero(2, second);
        String milliText = insertZero(3, milliSecond);
        
        return String.format("%s:%s:%s,%s", hourText, minuteText, secondText, milliText);
    }
    
    protected static String insertZero(int digit, int value) {
        String text = value + "";
        int offset = digit - text.length();
        for(int x = 0; x < offset; x++) {
            text = "0" + text;
        }
        return text;
    }
    
}
