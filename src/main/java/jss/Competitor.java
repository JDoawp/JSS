package jss;

public class Competitor {
    private String name;
    private String startTime;
    private String finishTime = null;
    private String time = null;
    private int timeOffset;


    Competitor(String name, int timeOffset){
        this.name = name;
        this.timeOffset = timeOffset;
    }

    public String getName() {
        return name;
    }
    public String getStartTime(){return startTime;}
    public String getFinishTime(){return finishTime;}
    public String getTime(){return time;}

    public void setName(String name){
        this.name = name;
    }
    public void setTime(String time){
        this.time = time;
    };

    @Override
    public String toString() {
        return name +" with timeOffset: " +timeOffset +" seconds.";
    }
}
