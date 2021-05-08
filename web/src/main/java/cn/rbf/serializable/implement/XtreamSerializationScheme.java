package cn.rbf.serializable.implement;

import cn.rbf.serializable.XMLSerializationScheme;
import cn.rbf.serializable.implement.xml.LXML;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author fk7075
 * @version 1.0
 * @date 2020/11/12 11:51
 */
public class XtreamSerializationScheme implements XMLSerializationScheme {

    @Override
    public String serialization(Object object) throws IOException {
        return new LXML().toXml(object);
    }

    @Override
    public Object deserialization(Type objectType, String objectStr) throws Exception {
        return new LXML().fromXml(objectStr);
    }
}
