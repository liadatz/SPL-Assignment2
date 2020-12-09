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
//------------------------------------fields----------------------------------------------

	private static class SingletonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> queuesMap;
	private final ConcurrentHashMap<Class<? extends Event<?>>, BlockingQueue<BlockingQueue<Message>>> eventSubscribers;
	private final ConcurrentHashMap<Class<? extends Broadcast>, BlockingQueue<BlockingQueue<Message>>> broadcastSubscribers;
	private ConcurrentHashMap<Event, Future> eventFutures;
	private final Object lock = new Object();
//--------------------------------constructors--------------------------------------------
	private MessageBusImpl() {
		queuesMap = new ConcurrentHashMap<>();
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
				if (!eventSubscribers.containsKey(type)) {
					eventSubscribers.put(type, new LinkedBlockingQueue<>());
				}
			}
			eventSubscribers.get(type).offer(queuesMap.get(m));
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (broadcastSubscribers) {
			if (!broadcastSubscribers.containsKey(type)) {
				broadcastSubscribers.put(type, new LinkedBlockingQueue<>());
			}
		}
		broadcastSubscribers.get(type).offer(queuesMap.get(m));

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
		synchronized (broadcastSubscribers) {
			if (broadcastSubscribers.containsKey(b.getClass()) && !(broadcastSubscribers.get(b.getClass()).isEmpty())) {
				BlockingQueue<BlockingQueue<Message>> polled = broadcastSubscribers.get(b.getClass());
				Iterator<BlockingQueue<Message>> iter = polled.iterator();
				while (iter.hasNext()) {
					BlockingQueue<Message> next = iter.next();
					synchronized (lock) {
						if (next != null)
							next.offer(b);
					}
				}
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (eventSubscribers) {
			if (eventSubscribers.containsKey(e.getClass())) {
				BlockingQueue<Message> polled = null;
				while (polled == null && !eventSubscribers.get(e.getClass()).isEmpty())
					polled = eventSubscribers.get(e.getClass()).poll();
				synchronized (lock) {
					if (polled != null) {
						polled.offer(e);
						eventSubscribers.get(e.getClass()).offer(polled);
						Future<T> future = new Future<T>();
						eventFutures.put(e, future);
						return future;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void register(MicroService m) {
		if (!isRegistered(m))
			queuesMap.put(m, new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		synchronized (lock) {
			if (m != null) {
				queuesMap.remove(m);//makes all m's message queues in eventSubscribers and broadcastSubscribers null
			}
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		if (m != null && queuesMap.containsKey(m)){
			return queuesMap.get(m).take(); //take is blocking method
		}
		return null;
	}

	private boolean isRegistered(MicroService m){
		return queuesMap.containsKey(m);
	}
}