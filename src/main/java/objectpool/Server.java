package objectpool;

import lombok.Data;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

@Data
 class TestChannel {

    private int id;

    public TestChannel(int id) throws InterruptedException {
        Thread.sleep(500);
        this.id = id;
    }
}
public class Server {

    public static KeyedPooledObjectFactory objectFactory = new MyKeyedPooledObjectFactory();
//    public static BaseKeyedPooledObjectFactory objectFactory = new MyKeyedPooledObjectFactory();

    public static GenericKeyedObjectPool<Integer, TestChannel> testClientKeyPool = new GenericKeyedObjectPool<>(objectFactory);
    public static int key1 = 1;
    public static int key2 = 2;
    public static int key3 = 3;

    public static void main(String[] args) throws Exception {
        test0();
        System.out.println("================================================");
        test1();
        System.out.println("================================================");
        test2();
        System.out.println("================================================");
        test3();
        System.out.println("================================================");
    }

    private static void test0() throws Exception {
        System.out.println("numIdle=" + testClientKeyPool.getNumIdle(key1));
        testClientKeyPool.addObject(key1);
        System.out.println("numIdle=" + testClientKeyPool.getNumIdle(key1));
        testClientKeyPool.clear(key1);
        System.out.println("numIdle=" + testClientKeyPool.getNumIdle(key1));
        testClientKeyPool.addObject(key1);
        System.out.println("numActive=" + testClientKeyPool.getNumActive(key1));
        testClientKeyPool.borrowObject(key1);
        System.out.println("numActive=" + testClientKeyPool.getNumActive(key1));
        testClientKeyPool.clear(key1);
        System.out.println("numActive=" + testClientKeyPool.getNumActive(key1));
    }

    private static void test1() throws Exception {
        long s1 = System.currentTimeMillis();
        TestChannel testChannel1 = testClientKeyPool.borrowObject(key1);
        System.out.println("time1=" + (System.currentTimeMillis() - s1));
        long s2 = System.currentTimeMillis();
        TestChannel testChannel2 = testClientKeyPool.borrowObject(key1);
        System.out.println("time2=" + (System.currentTimeMillis() - s2));
        testClientKeyPool.returnObject(key1, testChannel1);
        testClientKeyPool.returnObject(key1, testChannel2);
        System.out.println("testChannel1 == testChannel2 = " + (testChannel1 == testChannel2));
    }

    private static void test2() throws Exception {
        long s1 = System.currentTimeMillis();
        TestChannel testChannel1 = testClientKeyPool.borrowObject(key2);
        System.out.println("time1=" + (System.currentTimeMillis() - s1));
        testClientKeyPool.returnObject(key2, testChannel1);
        long s2 = System.currentTimeMillis();
        TestChannel testChannel2 = testClientKeyPool.borrowObject(key2);
        System.out.println("time2=" + (System.currentTimeMillis() - s2));
        testClientKeyPool.returnObject(key2, testChannel2);
        System.out.println("testChannel1 == testChannel2 = " + (testChannel1 == testChannel2));
    }

    private static void test3() throws Exception {
        long s1 = System.currentTimeMillis();
        TestChannel testChannel1 = testClientKeyPool.borrowObject(key3);
        System.out.println("time1=" + (System.currentTimeMillis() - s1));
        testClientKeyPool.invalidateObject(key3, testChannel1);
        long s2 = System.currentTimeMillis();
        TestChannel testChannel2 = testClientKeyPool.borrowObject(key3);
        System.out.println("time2=" + (System.currentTimeMillis() - s2));
        testClientKeyPool.invalidateObject(key3, testChannel2);
        System.out.println("testChannel1 == testChannel2 = " + (testChannel1 == testChannel2));
    }
}