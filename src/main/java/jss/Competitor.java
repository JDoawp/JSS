package jss;

public class Competitor {
    private String name;
    private String startTime;
    private int milliTime = 0;
    private String finishTime;
    public Timer timer;

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

    public int getMilliTime(){return milliTime;}

    public void setName(String name){
        this.name = name;
    }

    public void setFinishTime(){
        finishTime = timer.getTime();
    }
}
