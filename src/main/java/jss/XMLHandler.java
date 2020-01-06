package jss;

import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

//This class handles both all the XML stuff (using XStream) and holds the list of arrays.
public class XMLHandler{
    private XStream xstream = new XStream();
    private ArrayList<Competitor> competitorList = new ArrayList<>();

    XMLHandler(){
    }

    //Add some basic arrayList functions to the public scope.
    public void add(Competitor competitor){
        competitorList.add(competitor);
    }

    public Competitor get(int index){
        return competitorList.get(index);
    }

    public int size(){
        return competitorList.size();
    }

    //Sorts so the winner comes first
    public void sort(){
        Collections.sort(competitorList);
        System.out.println("Sorted by smallest elapsedTime");
    }

    //TODO Add actual comments here
    void save() {      //Uses XStreams ObjectOutput to save all Objects to a neatly formatted XML
        try {
            ObjectOutputStream output = xstream.createObjectOutputStream(new FileOutputStream("competitors.xml"));

            int i;  //Init outside for-loop to remember how many was stored.
            for(i = 0; i <= competitorList.size()-1; i++){
                output.writeObject(competitorList.get(i));
            }
            output.close();

            System.out.println("Saved " +i +" items to XML");
        }catch (IOException e){
            System.out.println("competitors.xml not found. ");
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")      //It's on purpose.
    boolean load() {        //Uses XStreams ObjectInput to load all Objects from an XML
        try{
            ObjectInputStream input = xstream.createObjectInputStream(new FileInputStream("competitors.xml"));
            int i = 0;  //Init outside while-loop to remember how many was stored
            try{
                while(true){
                    competitorList.add((Competitor)input.readObject());
                    competitorList.get(i).setSkiing(true);
                    competitorList.get(i).setStartClock(null);
                    competitorList.get(i).setFinishClock(null);
                    competitorList.get(i).setElapsedTimeDisplay(null);
                    i-=-1;      //i++ looks so unbalanced, this way it looks much better
                }
            }catch(EOFException | ClassNotFoundException e){
                System.out.println("No more objects in XML file... Added " +i +" objects.");
            }

            input.close();
            return true;
        }catch(IOException e){
            System.out.println("No XML file found.");
            return false;
        }
    }
}
