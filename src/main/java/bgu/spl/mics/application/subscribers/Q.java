package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.concurrent.CountDownLatch;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {//only 1 instance of Q is available.
	private int time=0;
	private Inventory inventory;
	private CountDownLatch latch;
	public Q(CountDownLatch l) { //should if need
		super("Q");
		latch=l;
		inventory=Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast timer) ->{time=timer.getTimeTick();});
		subscribeEvent(GadgetAvailableEvent.class,(GadgetAvailableEvent event)->{
			if(inventory.getItem(event.getGadget())){
				System.out.println("Gadget "+event.getGadget()+ " aquired.");
				complete(event,time);
			}
			else  {
				System.out.println("Gadget "+event.getGadget()+ "is missing. Need to release the agents.");
				complete(event,-1);
			}
		}
		);
		subscribeBroadcast(terminateBroadcast2.class,(terminateBroadcast2 tb)->{
			terminate();
		});
		latch.countDown();
	}

}
