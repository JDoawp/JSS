package jss;

public class Competitor {
    private String name;
    private String startTime;
    private String finishTime;
    private String time = null;


    Competitor(String name, String startTime, String finishTime){
        this.name = name;
        this.startTime = startTime;
        this.finishTime = finishTime;
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
}
