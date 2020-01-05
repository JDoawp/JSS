package jss;

public class Competitor {
    private String name;
    private String startClock = null;
    private String finishClock = null;
    private String elapsedTimeDisplay = null;
    private boolean skiing;
    private long elapsedTime;
    private int timeOffset;


    Competitor(String name, int timeOffset, boolean skiing){
        this.name = name;
        this.timeOffset = timeOffset;
        this.skiing = skiing;
    }

    public String getName() {
        return name;
    }
    public double getElapsedTime(){return elapsedTime;}
    public boolean isSkiing(){return skiing;}
    public String getFinishClock() {
        return finishClock;
    }
    public String getStartClock() {
        return startClock;
    }
    public String getElapsedTimeDisplay(){
        return elapsedTimeDisplay;
    }
    public int getTimeOffset(){return timeOffset;}

    public void setElapsedTime(long elapsedTime){
        this.elapsedTime = elapsedTime;
    }
    public void setSkiing(Boolean skiing){
        this.skiing = skiing;
    }
    public void setStartClock(String startClock){
        this.startClock = startClock;
    }
    public void setFinishClock(String finishClock){
        this.finishClock = finishClock;
    }

    public void setElapsedTimeDisplay(String elapsedTimeDisplay){
        this.elapsedTimeDisplay = elapsedTimeDisplay;
    }

    @Override
    public String toString() {
        return name +" with timeOffset: " +timeOffset +" seconds, their skiing status is: " +skiing;
    }
}
