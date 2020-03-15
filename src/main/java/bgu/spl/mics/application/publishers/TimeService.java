package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.terminateBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {//sends tickBroadCastMessege every 100ms.
	private int totalNumberOfTimeTicks;
	private int currTimeTick=0;
	public TimeService(int duration) {
		super("Time Service");
		totalNumberOfTimeTicks=duration;
	}
	@Override
	protected void initialize() {
	}
	@Override
	public void run() {
		while(currTimeTick<=totalNumberOfTimeTicks) {
			try {
				System.out.println("\n\ntime tick: "+currTimeTick);
				getSimplePublisher().sendBroadcast(new TickBroadcast(currTimeTick));
				currTimeTick++;
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		getSimplePublisher().sendBroadcast(new terminateBroadcast());
	}

}
