package objectpool;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;

public class MyKeyedPooledObjectFactory1 extends BaseKeyedPooledObjectFactory {
    @Override
    public Object create(Object key) throws Exception {
        return null;
    }

    @Override
    public PooledObject wrap(Object value) {
        return null;
    }
}
