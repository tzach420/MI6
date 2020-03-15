package bgu.spl.mics;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.mics.application.messages.MissionReceiveEvent;
import bgu.spl.mics.application.messages.terminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.subscribers.M;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static MessageBroker singleInstance = new MessageBrokerImpl();
	/**
	 * Retrieves the single instance of this class.
	 */
	private ConcurrentHashMap<Subscriber, BlockingQueue<Message>> messageQueueOfSubscribers = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class<? extends Message>, BlockingQueue<Subscriber>> roundRobinQueueOfEvent = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Event<?>, Future<?>> futurePerEvent = new ConcurrentHashMap<>();//every event have is own future
	private AtomicInteger numOfMThatTerminated=new AtomicInteger(0);


	public static synchronized MessageBroker getInstance() {
		return singleInstance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {//register subscribe "m" to event "type".
		//if not exsist add new queue for event type, FIXME check if linked save order for round rubin
		roundRobinQueueOfEvent.putIfAbsent(type, new LinkedBlockingDeque<>());
		roundRobinQueueOfEvent.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) { //register subscribe "m" to broadcast "type".
		roundRobinQueueOfEvent.putIfAbsent(type, new LinkedBlockingDeque<>());
		roundRobinQueueOfEvent.get(type).add(m);
	}
	@Override
	public <T> void complete(Event<T> e, T result) {//notify the messegeBroker event e was handled, with result result.
		Future<T> future =(Future<T> )futurePerEvent.get(e);
		future.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {//add this messege to the queue of relevant subscribes.
		if(b instanceof terminateBroadcast){
			for(Subscriber subscriber: messageQueueOfSubscribers.keySet()){///FIND ALL M INSTANCES AND UPDATE THEIR MISSION RECIEVE EVENT MESSAGES IN THEIR QUEUES.
				if(subscriber instanceof M){
					for(Message message: messageQueueOfSubscribers.get(subscriber)){
						if(message instanceof MissionReceiveEvent) {
							((MissionReceiveEvent) message).terminateHappened();
						}
					}
				}
			}
		}
		BlockingQueue<Subscriber> temp = roundRobinQueueOfEvent.get(b.getClass());//blocking queue of subscribers for messege b
		for (Subscriber subscriber : temp) {//for every subscribe who subscribes to Broadcast of b
				messageQueueOfSubscribers.get(subscriber).add(b); //add to that subsribe message b
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {//send event e to relevant subscribes using round robin, return future<t> that indicates the assignment progress.

		if (roundRobinQueueOfEvent.get(e.getClass()) == null) return null;//no exist subscribe who can handle this event
		synchronized(roundRobinQueueOfEvent.get(e.getClass())) {
			if (roundRobinQueueOfEvent.get(e.getClass()) == null || roundRobinQueueOfEvent.get(e.getClass()).isEmpty()) {
				return null;
			}
			Subscriber subscriberWhoHandleTheEvent = roundRobinQueueOfEvent.get(e.getClass()).poll();
			if (subscriberWhoHandleTheEvent == null) {
				return null;
			}
			Future<T> future = new Future<T>();
			futurePerEvent.put(e, future);
			messageQueueOfSubscribers.get(subscriberWhoHandleTheEvent).add(e);//add event to the queue of subscriberWhoHandleTheEvent
			roundRobinQueueOfEvent.get(e.getClass()).add(subscriberWhoHandleTheEvent);//add back subscriberWhoHandleTheEvent to the end of the queue, round robin
			return future;
	}
}

	@Override
	public void register(Subscriber m) {//createsd a queue for subscriber.
		messageQueueOfSubscribers.put(m,new LinkedBlockingDeque<>());
		System.out.println(m.getName()+" registered");
	}

	@Override
	public void unregister(Subscriber m) {//remove the queue of subscriber m and removes all the references realated to him.
		for(Class<? extends  Message> message: roundRobinQueueOfEvent.keySet()){
			if(roundRobinQueueOfEvent.get(message).contains(m)) roundRobinQueueOfEvent.get(message).remove(m); //remove m
		}
		for(Message message: messageQueueOfSubscribers.get(m)){ //no
			if(message.getClass()==Event.class)
				futurePerEvent.get(message).resolve(null); //check if need to add if statement
		}
		messageQueueOfSubscribers.remove(m);
		System.out.println(m.getName()+" unregistered");
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {//enable to take the next message.
		try {
			return messageQueueOfSubscribers.get(m).take();
		}
		catch (Exception e){
			return null;
		}
	}
}
