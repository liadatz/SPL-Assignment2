package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBus messageBus;
    private MicroService a;
    private Event event;
//    private Broadcast broadcast;


    @BeforeEach
    void setUp() {
        messageBus = new MessageBusImpl();
        a = new HanSoloMicroservice();
        event = new AttackEvent();
//        broadcast = new Broadcast();
    }

    @Test
    void register() {
        messageBus.register(a);
        assertTrue(messageBus.isExist(a));
    }

    @Test
    void subscribeEvent() {
        messageBus.register(a);
        messageBus.subscribeEvent(event.getClass(), a);
        Future f = messageBus.sendEvent(new DeactivationEvent());
        assertNull(f);
        isSent();

    }

    @Test
    void subscribeBroadcast() {
    }

    @Test
    void complete() {
        messageBus.register(a);
        messageBus.subscribeEvent(event.getClass(), a);
        Future f;
        f = messageBus.sendEvent(event);
        messageBus.complete(event, Boolean.TRUE);
        assertTrue(f.isDone());
        f = messageBus.sendEvent(event);
        messageBus.complete(event, Boolean.FALSE);
        assertFalse(f.isDone());
    }

    @Test
    void sendBroadcast() {
    }

    @Test
    void sendEvent() {
        messageBus.register(a);
        messageBus.subscribeEvent(event.getClass(), a);
        isSent();
    }

    private void isSent() {
        Message s = null;
        messageBus.sendEvent(event);
        try {
            s = messageBus.awaitMessage(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(s);
    }

    @Test
    void unregister() {
    }

    @Test
    void awaitMessage() {
        messageBus.register(a);
        try {
            messageBus.awaitMessage(a);
        } catch (InterruptedException e) {
            assertNotNull(e);
        }
        messageBus.subscribeEvent(event.getClass(), a);
        messageBus.sendEvent(event);
        Message s = null;
        try {
            s = messageBus.awaitMessage(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(s);
    }
}