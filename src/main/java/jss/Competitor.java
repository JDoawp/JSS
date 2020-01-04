package jss;

public class Competitor {
    private String name;
    private int milliTime = 0;
    private String finishTime;
    public Timer timer = new Timer();

    Competitor(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getMilliTime(){return milliTime;}

    public void setName(String name){
        this.name = name;
    }

    public void setFinishTime(){
        finishTime = timer.getTime();
    }
}
