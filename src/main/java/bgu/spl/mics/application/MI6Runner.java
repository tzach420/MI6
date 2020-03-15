package bgu.spl.mics.application;

import bgu.spl.mics.GsonHandler;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */



public class MI6Runner {
    public static void main(String[] args){
        try {
            initPassivesAndRunThreads(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Inventory.getInstance().printToFile(args[1]);
        Diary.getInstance().printToFile(args[2]);
    }
    public static void initPassivesAndRunThreads(String path) throws FileNotFoundException {
        Gson json= new Gson();
        JsonReader reader= new JsonReader(new FileReader(path));
        GsonHandler h=json.fromJson(reader,GsonHandler.class);
        h.initInventory();
        h.initSquad();
        CountDownLatch latch=new CountDownLatch(h.getNumOfThreads());
        ArrayList<Thread> threads=h.initThreads(latch);
        for(Thread t: threads)
            t.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread timeService= new Thread(new TimeService(h.getTime()));//INIT TIME SERVICE, ONLY AFTER ALL OTHER THREADS FINISHED INIT.
        timeService.start();
        for(Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
