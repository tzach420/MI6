package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
//import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int counterOfTotalMissions;//injcrement every time mission is accept.
	private int time = 0;
	private CountDownLatch latch;
	private Diary diary = Diary.getInstance();


	public M(String id,CountDownLatch l) {
		super("M" + id);
		latch=l;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast timer) -> {
			time = timer.getTimeTick();
		});

		subscribeEvent(MissionReceiveEvent.class, (MissionReceiveEvent event) -> {
			System.out.println(getName() + " Start to handle mission " + event.getMissionInfo().getMissionName());
			diary.incrementTotal();
			if(event.getTerminateHappened()){
				complete(event,null);
			}
			else {
				List<String> serials = event.getMissionInfo().getSerialAgentsNumbers();
				Future<Boolean> agentsFInishedMissionFuture= new Future<>();
				Future<Object[]> agentsAvailableFuture = getSimplePublisher().sendEvent(new AgentAvailableEvent(serials, event.getMissionInfo().getDuration(),agentsFInishedMissionFuture));
				if (!(agentsAvailableFuture.get()[0].toString()).equals("Agents not available")) {
					Future<Integer> gadgetAvailableFuture = getSimplePublisher().sendEvent(new GadgetAvailableEvent(event.getMissionInfo().getGadget()));//send event to q.
					Integer qTime = null;
					if (gadgetAvailableFuture != null) qTime = gadgetAvailableFuture.get();
					if (qTime != null && qTime.intValue() >= 0) {
						if (time <= event.getMissionInfo().getTimeExpired()) {
							System.out.println(getName() + " execute mission " + event.getMissionInfo().getMissionName());
							Future<Boolean> futureSendAgent = (Future<Boolean>) agentsAvailableFuture.get()[2];
							futureSendAgent.resolve(true);
							agentsFInishedMissionFuture.get();//wait till the agents finish the mission
							Report report = new Report(event.getMissionInfo(), (List<String>) agentsAvailableFuture.get()[1], (String) agentsAvailableFuture.get()[0], this.getName(), qTime, time);
							diary.addReport(report);
							complete(event, report);
							System.out.println("Mission " + event.getMissionInfo().getMissionName() + " finished successfully by "+getName());
						} else {
							complete(event, null);//event failed.
							System.out.println(getName() + " abort mission " + event.getMissionInfo().getMissionName() + ", time expired.");
							Future<Boolean> futureSendAgent = (Future<Boolean>) agentsAvailableFuture.get()[2];//free agents.
							futureSendAgent.resolve(false);
						}
					} else {//GADGET NOT AVAILABLE.
						Future<Boolean> futureSendAgent = (Future<Boolean>) agentsAvailableFuture.get()[2];
						futureSendAgent.resolve(false);
						complete(event, null);
					}
				}
				else {//AGENTS NOT AVAILABLE.
					Future<Boolean> futureSendAgent = (Future<Boolean>) agentsAvailableFuture.get()[2];
					futureSendAgent.resolve(false);
					complete(event, null); //aggent don't require
					System.out.println(getName() + " abort mission " + event.getMissionInfo().getMissionName() + ". Agents are missing.");
				}
			}
		});
		subscribeBroadcast(terminateBroadcast.class, (terminateBroadcast tb) -> {
			terminate();
			getSimplePublisher().sendBroadcast(new mFinishedTerminationBroadcast());
		});
		latch.countDown();
	}
}































