package jss;

public class Competitor {
    private String name;
    private String startClock = null;
    private String finishClock = null;
    private String elapsedTime = null;
    private boolean skiing = false;
    private long lastTime;
    private int timeOffset;


    Competitor(String name, int timeOffset){
        this.name = name;
        this.timeOffset = timeOffset;
    }

    public String getName() {
        return name;
    }
    public double getLastTime(){return lastTime;}
    public boolean isSkiing(){return skiing;}
    public String getFinishClock() {
        return finishClock;
    }
    public String getStartClock() {
        return startClock;
    }
    public String getElapsedTime(){
        return elapsedTime;
    }
    public int getTimeOffset(){return timeOffset;}

    public void setLastTime(long lastTime){
        this.lastTime = lastTime;
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

    public void setElapsedTime(String elapsedTime){
        this.elapsedTime = elapsedTime;
    }

    @Override
    public String toString() {
        return name +" with timeOffset: " +timeOffset +" seconds, their skiing status is: " +skiing;
    }
}
