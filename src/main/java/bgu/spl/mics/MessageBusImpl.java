package bgu.spl.mics;
import bgu.spl.mics.application.passiveObjects.RoundRobin;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
//------------------------------------fields----------------------------------------------


	private static class SingletonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}
	/**
	 * @param MicroservisesQueues contains the message {@link BlockingQueue} of all the registered {@link MicroService}
	 * @param eventSubscribers contains {@link Event} and the matching {@link RoundRobin} of the {@link MicroService} subscribed to it
	 * @param broadcastSubscribers contains {@link Broadcast} and the matching {@link ArrayList} of {@link MicroService} subscribed to it
	 * @param eventFuture contains all event which were sent and their matching {@link Future}
	 */
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> MicroservicesQueues;
	private final ConcurrentHashMap<Class<? extends Event<?>>, RoundRobin> eventSubscribers;
	private final ConcurrentHashMap<Class<? extends Broadcast>, ArrayList<MicroService>> broadcastSubscribers;
	private final ConcurrentHashMap<Event, Future> eventFutures;
//--------------------------------constructors--------------------------------------------
	private MessageBusImpl() {
		MicroservicesQueues = new ConcurrentHashMap<>();
		eventSubscribers = new ConcurrentHashMap<>();
		broadcastSubscribers = new ConcurrentHashMap<>();
		eventFutures = new ConcurrentHashMap<>();
	}
//----------------------------------getters-----------------------------------------------

	/**
	 * get instace of singleton thread safe message bus
	 * @return the single instace of the message bus
	 */
	public static MessageBusImpl getInstance(){
		return SingletonHolder.instance;
	}
//-----------------------------------methods----------------------------------------------

	/**
	 *
	 * @param type The type to subscribe to,
	 * @param m    The subscribing micro-service.
	 * @param <T> The type of the result expected by the completed event.
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m){
			synchronized (eventSubscribers) {
				// If no one subscribed to this event before
				// create new RoundRobin with 'm' and insert to eventSubscribers
				if (!eventSubscribers.containsKey(type)) {
					RoundRobin newRoundRobin = new RoundRobin();
					newRoundRobin.push(m);
					eventSubscribers.put(type, newRoundRobin);
				}
				// Insert 'm' to Event type RoundRobin
				else eventSubscribers.get(type).push(m);
			}
	}

	/**
	 *
	 * @param type 	The type to subscribe to.
	 * @param m    	The subscribing micro-service.
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (broadcastSubscribers) {
			// If no one subscribed to this broadcast before
			// create new list with 'm' and insert to broadcastSubscribers
			if (!broadcastSubscribers.containsKey(type)) {
				ArrayList<MicroService> newArrayList = new ArrayList<>();
				newArrayList.add(m);
				broadcastSubscribers.put(type, newArrayList);
			}
			// Insert 'm' to Broadcast type list
			else broadcastSubscribers.get(type).add(m);
		}
	}

	/**
	 *
	 * @param e      The completed event.
	 * @param result The resolved result of the completed event.
	 * @param <T> The type of the result expected by the completed event.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
			if (eventFutures.containsKey(e)) {
				eventFutures.get(e).resolve(result);
			}
	}

	/**
	 *
	 * @param b 	The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (broadcastSubscribers) {
			// Check if any MicroService registered to 'b' type
			// & if 'b' type list contains a Microservice at the moment
			if (broadcastSubscribers.containsKey(b.getClass()) && !(broadcastSubscribers.get(b.getClass()).isEmpty())) {
				// add 'b' Message to all Microservices Queues
				for (MicroService m : broadcastSubscribers.get(b.getClass())) {
					MicroservicesQueues.get(m).offer(b);
				}
			}
		}
	}

	/**
	 *
	 * @param e     	The event to add to the queue.
	 * @param <T> The type of the result expected by the completed event.
	 * @return the {@link Future} matching for this {@link Event}. the future will be resolved when the event is done
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (eventSubscribers) {
			// Check if any MicroService registered to 'e' type
			// & if 'e' RoundRobin contains a Microservice at the moment
			if (eventSubscribers.containsKey(e.getClass()) && !eventSubscribers.get(e.getClass()).isEmpty()) {
				Future<T> future = new Future<>();
				eventFutures.put(e, future);
				// insert Message to the next available MicroServices
				MicroService first = eventSubscribers.get(e.getClass()).pop();
				MicroservicesQueues.get(first).offer(e);
				return future;
			}
		}
		return null;
	}

	/**
	 * creates new {@link Message} {@link BlockingQueue} for m
	 * @param m the micro-service to create a queue for.
	 */
	@Override
	public void register(MicroService m) {
		//System.out.println(m.getName() + " is registering"); // (delete before submission)
		// Check if 'm' registered before and if not add add 'm' Messages Queue to MicroservicesQueues
		MicroservicesQueues.putIfAbsent(m, new LinkedBlockingQueue<>());
	}

	/**
	 * deletes m from all the related queues and maps
	 * @param m the micro-service to unregister.
	 */
	@Override
	public void unregister(MicroService m) {
		System.out.println(m.getName() + " is unregistering"); // (delete before submission)
			// Remove 'm' from all RoundRobins
			synchronized (eventSubscribers) {
				for (RoundRobin currentRoundRobin : eventSubscribers.values()) {
					currentRoundRobin.remove(m);
				}
			}
			// Remove 'm' from all broadcasts
			synchronized (broadcastSubscribers) {
				for (ArrayList<MicroService> currentArrayList : broadcastSubscribers.values()) {
					currentArrayList.remove(m);
				}
			}
			// Remove 'm' from MicroservicesQueues
			MicroservicesQueues.remove(m);
	}

	/**
	 *
	 * @param m The micro-service requesting to take a message from its message
	 *          queue.
	 * @return the next {@link Message} in m's {@link Message} {@link BlockingQueue}
	 * @throws InterruptedException
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// Check if 'm' is registered, if so take a Message from his Messages Queue
		if (MicroservicesQueues.containsKey(m)) {
			return MicroservicesQueues.get(m).take(); // take is blocking method
		}
		return null;
	}
}
