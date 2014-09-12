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
    
    public Time(int hour, int minute, int second, int milliSecond) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        allMilliSecond = (milliSecond) + (second * 1000) + (minute * 60 * 1000) + (hour * 60 * 60 * 1000);
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
        hour = allMilliSecond % (60 * 60 * 1000);
        minute = allMilliSecond % (60 * 1000);
        second = allMilliSecond % 1000;
    }
    
    public String toSRTFomat() {
        String hourText = insertZero(2, hour);
        String minuteText = insertZero(2, minute);
        String secondText = insertZero(2, second);
        String milliText = insertZero(3, milliSecond);
        
        return String.format("%s:%s:%s,%s", hourText, minuteText, secondText, milliText);
    }
    
    public static String insertZero(int digit, int value) {
        String text = value + "";
        int offset = text.length() - digit;
        for(int x = 0; x < offset; x++) {
            text = "0" + text;
        }
        return text;
    }
    
}
