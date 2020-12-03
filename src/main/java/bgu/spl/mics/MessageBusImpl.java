package bgu.spl.mics;

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
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> queuesMap;
	private ConcurrentHashMap<Class<? extends Event<?>>, LinkedBlockingQueue<LinkedBlockingQueue<Message>>> eventSubscribers;
	private ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingQueue<LinkedBlockingQueue<Message>>> broadcastSubscribers;
	private ConcurrentHashMap<Event<?>, Future<Object>> eventFutures; //is <Object> ok? (fixes line 61)
	private static MessageBusImpl instance = null;

	private MessageBusImpl() {
		queuesMap = new ConcurrentHashMap<>();
		eventSubscribers = new ConcurrentHashMap<>();
		broadcastSubscribers = new ConcurrentHashMap<>();
		eventFutures = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance(){
		if (instance == null) {
			instance = new MessageBusImpl();
		}
		return instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m){
		synchronized (eventSubscribers) { //??
			if (!eventSubscribers.containsKey(type)) {
				eventSubscribers.put(type, new LinkedBlockingQueue<>());
			}
			try {eventSubscribers.get(type).put(queuesMap.get(m));}
			catch (InterruptedException e){}
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (broadcastSubscribers) { //??
			if (!broadcastSubscribers.containsKey(type)) {
				broadcastSubscribers.put(type, new LinkedBlockingQueue<>());
			}
			try {broadcastSubscribers.get(type).put(queuesMap.get(m));}
			catch (InterruptedException e){}
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
			if (eventFutures.containsKey(e)){
				eventFutures.get(e).resolve(result);
			}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (broadcastSubscribers.containsKey(b.getClass())){
			LinkedBlockingQueue<LinkedBlockingQueue<Message>> polled = broadcastSubscribers.get(b.getClass());
			Iterator<LinkedBlockingQueue<Message>> iter = polled.iterator();
			while(iter.hasNext()){
				try{iter.next().put(b);}
				catch(InterruptedException e){}
			}
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {

		if (eventSubscribers.containsKey(e.getClass())) {
			LinkedBlockingQueue<Message> polled = eventSubscribers.get(e.getClass()).poll();
			if(polled != null){
				try {polled.put(e);}
				catch (InterruptedException ex) {}
				try {eventSubscribers.get(e.getClass()).put(polled);}
				catch (InterruptedException ex) {}
				Future<T> future = new Future<T>();
				eventFutures.put(e, future);
				return future;
				}
			}
		return null;
	}

	@Override
	public void register(MicroService m) {
		queuesMap.put(m, new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		return null;
	}
}