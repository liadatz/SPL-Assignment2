package bgu.spl.mics;

import bgu.spl.mics.application.messages.DummyBroadcast;
import bgu.spl.mics.application.messages.DummyEvent;
import bgu.spl.mics.application.services.DummyMicroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBusImpl messageBus;
    private DummyMicroService a;
    private DummyEvent dummyEvent;


    @BeforeEach
    void setUp() {
        messageBus = MessageBusImpl.getInstance();
        a = new DummyMicroService("a");
        dummyEvent = new DummyEvent("em");
    }

    @Test
    void testEvents() { //test for methods 'subscribeEvent', 'sendEvent', 'awaitMessage', 'register'
        messageBus.register(a);
        messageBus.subscribeEvent(DummyEvent.class, a);
        messageBus.sendEvent(dummyEvent);
        isSent(a, dummyEvent); //use of self- aid test method
    }

    @Test
    void complete() {
        messageBus.register(a);
        //first testing completed event
        messageBus.subscribeEvent(DummyEvent.class, a);
        Future f= messageBus.sendEvent(dummyEvent);
        messageBus.complete(dummyEvent, Boolean.TRUE);
        assertTrue(f.isDone());
        //then testing non- completed event
        f = messageBus.sendEvent(dummyEvent);
        messageBus.complete(dummyEvent, Boolean.FALSE);
        assertFalse((Boolean) f.get());
    }

    @Test
    void sendBroadcast() {
        //create and register 2 Microservices
        DummyMicroService b = new DummyMicroService("b");
        messageBus.register(a);
        messageBus.register(b);
        //subscribe a and b to receive 'DummyBroadcast' messages
        messageBus.subscribeBroadcast(DummyBroadcast.class,a);
        messageBus.subscribeBroadcast(DummyBroadcast.class,b);
        DummyBroadcast dummyBroadcast = new DummyBroadcast("bm");
        messageBus.sendBroadcast(dummyBroadcast);
        //tests if the broadcast message was received properly
        isSent(a, dummyBroadcast);
        isSent(b, dummyBroadcast);
    }

    private void isSent(MicroService microService, Message expectedMessage) { //test if a message was received properly after been sent
        Message s = null;
        try {
            s = messageBus.awaitMessage(microService);
            assertEquals(expectedMessage,s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

