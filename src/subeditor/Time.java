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
    
    public void updateGeneral() {
        hour = allMilliSecond % (60 * 60 * 1000);
        minute = allMilliSecond % (60 * 1000);
        second = allMilliSecond % 1000;
    }
    
}
