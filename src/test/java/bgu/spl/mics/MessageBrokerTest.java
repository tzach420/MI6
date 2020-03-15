package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import bgu.spl.mics.application.subscribers.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.publishers.*;
import bgu.spl.mics.Message;
import bgu.spl.mics.Subscriber;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    private MessageBroker messageBroker ;
    CountDownLatch latch=new CountDownLatch(5);
    MissionInfo missionInfo=new MissionInfo();

    @BeforeEach
    public void setUp() {
        messageBroker = MessageBrokerImpl.getInstance();
    }

    @Test
    public void subscribeEventTest() {
        Subscriber m1 = new M("1", latch);
        Event<Report> event1 = new MissionReceiveEvent(missionInfo);
        messageBroker.register(m1);
        messageBroker.subscribeEvent(MissionReceiveEvent.class,m1);
        messageBroker.sendEvent(event1);
        try {
            Message message = messageBroker.awaitMessage(m1);
            assertTrue(message!=null,"message should not be null");
        }
        catch(Exception e1){
            fail("no should be an exception");
        }
    }


    @Test
    public void subscribeBroadcastTest(){
          Subscriber m2=new M("2",latch);
          Broadcast broadcast=new TickBroadcast(5);
          messageBroker.register(m2);
          messageBroker.subscribeBroadcast(TickBroadcast.class,m2);
          messageBroker.sendBroadcast(broadcast);
        try {
            Message message = messageBroker.awaitMessage(m2);
            assertTrue(message!=null,"message should not be null");
        }
        catch(Exception e1){
            fail("no should be an exception");
        }
    }

    @Test
    public void completeTest(){

        Event<Integer> event2 = new GadgetAvailableEvent("d");
        Subscriber q3 = new Q(latch);
        Future<Integer> future=new Future<>();
        messageBroker.register(q3);
        messageBroker.subscribeEvent(GadgetAvailableEvent.class,q3);
        future = messageBroker.sendEvent(event2);
        try {
            Message message = messageBroker.awaitMessage(q3);
            if(message!=null)
            messageBroker.complete(event2,new Integer(5));
            assertEquals(future.get(),new Integer(5),"future need to get the right resolve");
        }
        catch(Exception e1){
            fail("no should be an exception");
        }
    }

    public void sendBroadcastTest() {

        Subscriber q = new Q(latch);
        messageBroker.register(q);
        messageBroker.subscribeBroadcast(TickBroadcast.class, q);
        Broadcast broadcast = new TickBroadcast(7);
        messageBroker.sendBroadcast(broadcast);
        try {
            Message message = messageBroker.awaitMessage(q);
            if (message != null && message instanceof TickBroadcast)
               assertEquals (7,((TickBroadcast) message).getTimeTick(),"message should be 7");

        } catch (Exception e1) {
            fail("no should be an exception");

        }
    }

    public void sendEventTest(){
        MissionInfo missionInfo=new MissionInfo();
        Subscriber m5 = new M("5",latch);
        Subscriber m6 = new M("6",latch);
        messageBroker.register(m5);
        messageBroker.register(m6);
        Event<Report> event5 = new MissionReceiveEvent(missionInfo);
        Event<Report> event6 = new MissionReceiveEvent(missionInfo);
        messageBroker.subscribeEvent(MissionReceiveEvent.class, m5);
        messageBroker.subscribeEvent(MissionReceiveEvent.class, m6);
        messageBroker.sendEvent(event5);
        messageBroker.sendEvent(event6);
        try {
            Message message5 = messageBroker.awaitMessage(m5);
            Message message6 = messageBroker.awaitMessage(m6);
                assertTrue (message5 instanceof  MissionReceiveEvent && message6 instanceof  MissionReceiveEvent,"Round robin check");

        } catch (Exception e1) {
            fail("no should be an exception, round robin failed");

        }
    }

    public void registerTest(){
        Message message=new GadgetAvailableEvent("a") ;
        Subscriber q = new Q(latch);
        messageBroker.register(q);
        messageBroker.subscribeEvent(GadgetAvailableEvent.class,q);
        Event<Integer> event = new GadgetAvailableEvent("a");
        messageBroker.sendEvent(event);
        try{
            messageBroker.awaitMessage(q);
            assertTrue(true);
        }
        catch (Exception e){
            fail("no should be an exception");
        }


    }
    public void unregisterTest(){
        Subscriber m7 = new M("6",latch);
        messageBroker.register(m7);
        messageBroker.unregister(m7);
        try{
            messageBroker.awaitMessage(m7);
            fail("should be an exception");
        }
        catch (Exception e){
            assertTrue(true,"no possible to sub wait for message if he unregister");
        }

    }



    public void awaitMessageTest(){
        Subscriber m4=new M("35",latch);
        messageBroker.register(m4);
        messageBroker.subscribeEvent(MissionReceiveEvent.class,m4);
        messageBroker.sendEvent(new MissionReceiveEvent(new MissionInfo()));
        try {
            Message message = messageBroker.awaitMessage(m4);
           assertTrue(message!=null,"mwssage should not be null");
        }
        catch(Exception e) {
            fail("no should be an exception");
        }
    }


}
