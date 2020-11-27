package bgu.spl.mics;

import bgu.spl.mics.application.messages.DummyBroadcast;
import bgu.spl.mics.application.messages.DummyEvent;
import bgu.spl.mics.application.services.DummyMicroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBus messageBus;
    private DummyMicroService a;
    private DummyEvent dummyEvent;


    @BeforeEach
    void setUp() {
        messageBus = new MessageBusImpl();
        a = new DummyMicroService("a");
        dummyEvent = new DummyEvent("em");
    }

    @Test
    void register() {
        messageBus.register(a);
        assertTrue(messageBus.isExist(a));
    }

    @Test
    void testEvents() {
        messageBus.register(a);
        messageBus.subscribeEvent(DummyEvent.class, a);
        messageBus.sendEvent(dummyEvent);
        isSent(a, "em");
    }

    @Test
    void complete() {
        messageBus.register(a);
        messageBus.subscribeEvent(DummyEvent.class, a);
        Future f;
        f = messageBus.sendEvent(dummyEvent);
        messageBus.complete(dummyEvent, Boolean.TRUE);
        assertTrue(f.isDone());
        f = messageBus.sendEvent(dummyEvent);
        messageBus.complete(dummyEvent, Boolean.FALSE);
        assertFalse(f.isDone());
    }

    @Test
    void sendBroadcast() {
        messageBus.register(a);
        DummyBroadcast dummyBroadcast = new DummyBroadcast("bm");
        messageBus.subscribeBroadcast(DummyBroadcast.class,a);
        DummyMicroService b = new DummyMicroService("b");
        messageBus.subscribeBroadcast(DummyBroadcast.class,b);
        messageBus.sendBroadcast(dummyBroadcast);
        isSent(a, "a");
        isSent(b, "b ");
    }

    private void isSent(MicroService microService, String expectedMessage) {
        Message s = null;
        try {
            s = messageBus.awaitMessage(microService);
            assertEquals(s.getMessage(),expectedMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//test