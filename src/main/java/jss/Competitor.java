package jss;

public class Competitor {
    private String name;
    private String lasttime = null;
    private String startTime = null;
    private String finishTime = null;
    private boolean skiing = false;
    private int timeOffset;


    Competitor(String name, int timeOffset){
        this.name = name;
        this.timeOffset = timeOffset;
    }

    public String getName() {
        return name;
    }
    public String getLasttime(){return lasttime;}
    public boolean isSkiing(){return skiing;}
    public String getFinishTime() {
        return finishTime;
    }
    public String getStartTime() {
        return startTime;
    }


    public void setName(String name){
        this.name = name;
    }
    public void setLasttime(String lasttime){
        this.lasttime = lasttime;
    }
    public void setSkiing(Boolean skiing){
        this.skiing = skiing;
    }

    @Override
    public String toString() {
        return name +" with timeOffset: " +timeOffset +" seconds, their skiing status is: " +skiing;
    }
}
