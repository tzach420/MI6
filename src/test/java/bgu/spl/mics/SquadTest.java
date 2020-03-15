package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    Squad s;
    Agent[] agents= {new Agent("001","james"),new Agent("002","arthur")};
    List<String> serials=new LinkedList<>();
    List<String> names=new LinkedList<>();
    @BeforeEach
    public void setUp(){
       s= Squad.getInstance();
        serials.add("001");
        serials.add("002");
        names.add("james");
        names.add("arthur");
    }

    @Test
    public void getInstanceTest(){
        Squad j=Squad.getInstance();
        assertEquals(s,j,"Failed to create only 1 instance of squad");
    }
    @Test
    public void loadTest(){
       s.load(agents);
        List<String> names=s.getAgentsNames(serials);
        List<String> expected=new LinkedList<>();
        expected.add("james");
        expected.add("arthur");
        assertEquals(expected,names,"all names should be the same");//need to check if assert is good for lists.
        }
    @Test
    public void releaseAgentsTest() {
        s.load(agents);
        s.releaseAgents(serials);
        //assertEquals(,s.getAgentsNames(serials),"Did not release all the agents");

    }
    @Test
    public void getAgentsNamesTest() {
        s.load(agents);
        List<String> toCheck=s.getAgentsNames(serials);
        assertEquals(names,toCheck,"Should contain all the agents names.");
    }





    }

