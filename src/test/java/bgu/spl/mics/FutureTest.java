package bgu.spl.mics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;


public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @Test
    public void testGet()
    {
        assertFalse(future.isDone());
        future.resolve("expectedAnswer");
        String answer = future.get();
        assertEquals(answer, "expectedAnswer");
    }

    @Test
    public void testResolve(){
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertEquals(str, future.get());
    }

    @Test
    public void testIsDone(){
        String str = "someResult";
        assertFalse(future.isDone());
        future.resolve(str);
        assertTrue(future.isDone());
    }

    @Test
    public void testGetWithTimeOut() throws InterruptedException
    {
        assertFalse(future.isDone());
        assertNull(future.get(100,TimeUnit.MILLISECONDS));
        future.resolve("expectedAnswer");
        assertEquals(future.get(100,TimeUnit.MILLISECONDS),"expectedAnswer");
    }
}
