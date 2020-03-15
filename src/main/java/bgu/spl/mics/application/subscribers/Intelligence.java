package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceiveEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.terminateBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private List<MissionInfo> missionList;
	private CountDownLatch latch;
	public Intelligence(List<MissionInfo> list, int id,CountDownLatch l) {
		super("Inteligence"+id);
		latch=l;
		missionList=list;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
			for (MissionInfo mission: missionList) {
				if (tick.getTimeTick()==mission.getTimeIssued()) {
					System.out.println("Mission "+mission.getMissionName()+" was sent from "+getName());
					getSimplePublisher().sendEvent(new MissionReceiveEvent(mission));
				}
			}
		});
		subscribeBroadcast(terminateBroadcast.class,(terminateBroadcast tb)->{
			terminate();
		});
		latch.countDown();
	}


}
