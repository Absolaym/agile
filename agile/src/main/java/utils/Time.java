package utils;

/**
 * A wrapper class around the time (since most of the Date object is now deprecated)
 * The atomic time is the second to fit the need 
 * @author johnny
 *
 */
public class Time {
    /**
     * Time in seconds
     */
    public int time = 0;

    public Time() {
        this.time = 0;
    }

    public Time(int seconds) {
        this.time = seconds;
    }

    public Time(int hour, int minute, int second) {
        this.addHours(hour);
        this.addMinutes(minute);
        this.addSeconds(second);
    }
    
    public Time(Time t){
        this(t.time);
    }

    public void addHours(int hours) {
        this.time += 3600 * hours;
    }

    public void addMinutes(int minutes) {
        this.time += 60 * minutes;
    }

    public void addSeconds(int seconds) {
        this.time += seconds;
    }

    public Time ellapsedFrom(Time time) {
        return new Time(this.time - time.time);	
    }

    public int getHours() {
        return (this.time / 3600) % 24;
    }

    public int getMinutes() {
        return (this.time / 60) % 60;
    }

    public int getSeconds() {
        return this.time % 60;
    }

    public String toString() {
        return String.format("%02d", this.getHours()) 
                + ":" + String.format("%02d", this.getMinutes()) 
                + ":" + String.format("%02d", this.getSeconds());
    }
}
