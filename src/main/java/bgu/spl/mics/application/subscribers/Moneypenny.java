package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.Future;

import java.util.concurrent.CountDownLatch;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {//can be more than 1 moneypenny instance.
	private String serialNumber;
	private Object [] arreyReturnToM;
	private Future<Boolean> futureSendToM= new Future<>();//this futureSendToM will be  in the future true <=> moneyPenny should send the agents.
	private Squad squad;
	private int time=0;
	private CountDownLatch latch;

	public Moneypenny(String serialNumber,CountDownLatch l) {
		super("MP"+serialNumber);
		// object[0]=serial number of monneypenny or "agents is not available", object[1]=name of Agent, object[2]=future to M
		arreyReturnToM=new Object[3];
		arreyReturnToM[0]=serialNumber;
		arreyReturnToM[2]=futureSendToM;
		squad=Squad.getInstance();
		latch=l;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class,(TickBroadcast timer)->{time=timer.getTimeTick();});
		subscribeEvent(AgentAvailableEvent.class, (AgentAvailableEvent event)->{
			try {
				System.out.println(getName()+" checks if agents "+ event.getSerials().toString()+" available.");
				boolean agentAquired= squad.getAgents(event.getSerials());
				if(agentAquired) {
					System.out.println(getName()+" lock the agents "+event.getSerials().toString());
					arreyReturnToM[1]=squad.getAgentsNames(event.getSerials());
					complete(event,arreyReturnToM);
					if((boolean)futureSendToM.get()){//IF M GOT THE GADGET FOR THE MISSION.
						try {
							squad.sendAgents(event.getSerials(),event.getTimeToSend());//cannot fail.
							event.isAgentsFinishedMission().resolve(true);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else {//GADGET IS MISSING
						squad.releaseAgents(event.getSerials());
						System.out.println(getName()+" release agents "+event.getSerials().toString());
					}
				}
				else{//AGENTS ARE MISSING.
					arreyReturnToM[0]="Agents not available";
					complete(event,arreyReturnToM);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		subscribeBroadcast(terminateBroadcast2.class,(terminateBroadcast2 tb)->{
			terminate();
		});
		latch.countDown();
	}

}
