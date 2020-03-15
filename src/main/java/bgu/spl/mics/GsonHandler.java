package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class GsonHandler {
    private String[] inventory;
    private services services;
    private Agent[] squad;
   class services{
       int M;
       int Moneypenny;
       intelligence[] intelligence;
        int time;
    }
    class intelligence{
        MissionInfo[] missions;
    }
    public int getTime(){return services.time;}
    public void initInventory(){
       Inventory.getInstance().load(inventory);
    }
    public void initSquad(){
       Agent[] toInsert=new Agent[squad.length];
       int i=0;
       for(Agent agent: squad){
           toInsert[i]=new Agent(agent.getSerialNumber(),agent.getName());
           i++;
       }
       Squad.getInstance().load(toInsert);
    }

    /**
     *
     * @return the total number of threads we need to Init.
     */
    public int getNumOfThreads(){
       return services.M+services.Moneypenny+ services.intelligence.length+1;
    }

    /**
     *
     * @param latch
     * @return An arrayList of all the threads in the program.(except TimeService).
     */
    public ArrayList<Thread> initThreads(CountDownLatch latch){
       ArrayList<Thread> threads=new ArrayList<Thread>();
       for(int i=1;i<=services.M;i++){
           Thread t = new Thread(new M(String.valueOf(i),latch));
           threads.add(t);
       }
        for(int i=1;i<=services.Moneypenny;i++){
            Thread t = new Thread(new Moneypenny(String.valueOf(i),latch));
            threads.add(t);
        }
        int id=1;
        for(intelligence inte : services.intelligence){
            Thread t = new Thread(new Intelligence(Arrays.asList(inte.missions),id,latch));
            threads.add(t);
            id++;
        }
        threads.add( new Thread(new Q(latch)));
        threads.add(new Thread(new deathAngel(services.M)));
        return threads;
    }



}
