package objectpool;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class MyKeyedPooledObjectFactory implements KeyedPooledObjectFactory<Integer, TestChannel> {

    @Override
    public PooledObject<TestChannel> makeObject(Integer key) throws Exception {
        TestChannel channel = new TestChannel(key);
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public void destroyObject(Integer key, PooledObject<TestChannel> p) throws Exception {
        // Here, you can add any cleanup code if needed.
        // For this simple example, there's nothing to clean up.
    }

    @Override
    public boolean validateObject(Integer key, PooledObject<TestChannel> p) {
        // Here, you can add validation code to check if the object is still valid.
        // For this simple example, we'll always return true.
        return true;
    }

    @Override
    public void activateObject(Integer key, PooledObject<TestChannel> p) throws Exception {
        // This method can be used to "activate" or "initialize" an object before it's borrowed from the pool.
        // For this simple example, there's nothing to activate.
    }

    @Override
    public void passivateObject(Integer key, PooledObject<TestChannel> p) throws Exception {
        // This method can be used to "passivate" or "reset" an object before it's returned to the pool.
        // For this simple example, there's nothing to passivate.
    }
}
