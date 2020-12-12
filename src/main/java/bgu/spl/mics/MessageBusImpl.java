package bgu.spl.mics;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.RoundRobin;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
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
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> MicroservicesQueues;
	private final ConcurrentHashMap<Class<? extends Event<?>>, RoundRobin> eventSubscribers;
	private final ConcurrentHashMap<Class<? extends Broadcast>, ArrayList<MicroService>> broadcastSubscribers;
	private ConcurrentHashMap<Event, Future> eventFutures;
//--------------------------------constructors--------------------------------------------
	private MessageBusImpl() {
		MicroservicesQueues = new ConcurrentHashMap<>();
		eventSubscribers = new ConcurrentHashMap<>();
		broadcastSubscribers = new ConcurrentHashMap<>();
		eventFutures = new ConcurrentHashMap<>();
	}
//----------------------------------getters-----------------------------------------------
	public static MessageBusImpl getInstance(){
		return SingletonHolder.instance;
	}
//-----------------------------------methods----------------------------------------------
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m){
			synchronized (eventSubscribers) {
				// if no one subscribed to this event before
				if (!eventSubscribers.containsKey(type)) {
					RoundRobin newRoundRobin = new RoundRobin();
					newRoundRobin.push(m);
					eventSubscribers.put(type, newRoundRobin);
				}
				else eventSubscribers.get(type).push(m);
			}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (broadcastSubscribers) {
			if (!broadcastSubscribers.containsKey(type)) {
				ArrayList<MicroService> newArrayList = new ArrayList<>();
				newArrayList.add(m);
				broadcastSubscribers.put(type, newArrayList);
			}
			else broadcastSubscribers.get(type).add(m);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
			if (eventFutures.containsKey(e)) {
				eventFutures.get(e).resolve(result);
			}

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (broadcastSubscribers) {
			if (broadcastSubscribers.containsKey(b.getClass()) && !(broadcastSubscribers.get(b.getClass()).isEmpty())) {
				for (MicroService m : broadcastSubscribers.get(b.getClass())) {
					MicroservicesQueues.get(m).offer(b);
				}
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (eventSubscribers) {
			if (eventSubscribers.containsKey(e.getClass()) && !eventSubscribers.get(e.getClass()).isEmpty()) {
				MicroService first = eventSubscribers.get(e.getClass()).pop();
				MicroservicesQueues.get(first).offer(e);
				Future<T> future = new Future<T>();
				eventFutures.put(e, future);
				return future;
			}
		}
		return null;
	}

	@Override
	public void register(MicroService m) {
		//System.out.println(m.getName() + " is registering"); // log
		if (!isRegistered(m))
			MicroservicesQueues.put(m, new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		//System.out.println(m.getName() + " is unregistering"); // log
		if (m != null) {
			// Remove 'm' from MicroservicesQueues
			MicroservicesQueues.remove(m);
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
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
			if (m != null && MicroservicesQueues.containsKey(m)) {
				System.out.println("num of messages for "+m.getName()+": "+MicroservicesQueues.get(m).size());
				return MicroservicesQueues.get(m).take(); //take is blocking method
			}
			return null;

	}

	private boolean isRegistered(MicroService m){
		return MicroservicesQueues.containsKey(m);
	}

}
