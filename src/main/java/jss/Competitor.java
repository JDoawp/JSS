package jss;

class Competitor {
    private String name;
    private int milliTime = 0;

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
}
