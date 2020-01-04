package jss;

class Timer {
    private String date;
    private String time;
    private int startTime;
    private String endTime;

    Timer(){
        this(null, null, 0);
    }

    Timer(String date, String time, int startTime){
        this.date = date;
        this.time = time;
        this.startTime = startTime;
    }

    public String getTime(){
        return time;
    }

    public int getStartTime() {
        return startTime;
    }

    public String getDate() {
        return date;
    }

    void setTime(double time){
        this.time = ((int) time / 60 / 60 +":" +((int) time / 60) +":" + time % 60);    //Take in time as double, then convert it to a string in the format of Hours/Minutes/Seconds
    }
}
