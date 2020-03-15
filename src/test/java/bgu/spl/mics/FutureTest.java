package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    Future f=new Future<>();
    @BeforeEach
    public void setUp(){

    }

    @Test
    public void resolveTest(){
        assertEquals(false,f.isDone(),"resolved should be false.");
        f.resolve(6);
        assertEquals(true,f.isDone(),"resolved should be true.");
        assertEquals(6,f.get(),"Data should be equal");
    }
    @Test
    public void isDoneTest(){
        assertEquals(false,f.isDone(),"Should be false.");
        f.resolve(6);
        assertEquals(true,f.isDone(),"Should be true");
    }
    @Test
    public void getTest(){
        long timeout=30;
        TimeUnit unit=TimeUnit.MILLISECONDS;
        assertEquals(null,f.get(timeout,unit));
        f.resolve(6);//stimulates time.
        assertEquals(6,f.get(timeout,unit));
    }

}
