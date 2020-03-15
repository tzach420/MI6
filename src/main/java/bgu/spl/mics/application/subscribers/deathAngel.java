package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * this class responsible to make sure all the moneyPennys and Q terminate
 * only after all the Ms in the program terminated
 */
public class deathAngel extends Subscriber {
   private int numOfTotalM;
   private int numOfMthatTerminated;


    public deathAngel(int num) {
        super("deathAngel");
        numOfTotalM=num;
        numOfMthatTerminated=0;

    }
    @Override
    protected void initialize() {
        subscribeBroadcast(mFinishedTerminationBroadcast.class,(mFinishedTerminationBroadcast broadcast)-> {
           numOfMthatTerminated++;
           if(numOfMthatTerminated==numOfTotalM) getSimplePublisher().sendBroadcast(new terminateBroadcast2());//if all the M terminated, send msg to Q and MoneyPenny to terminate.
        }
        );
        subscribeBroadcast(terminateBroadcast2.class,(terminateBroadcast2 tb)->{
            terminate();
        });
    }
}
