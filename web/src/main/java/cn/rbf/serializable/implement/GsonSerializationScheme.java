package cn.rbf.serializable.implement;

import cn.rbf.serializable.JSONSerializationScheme;
import cn.rbf.serializable.json.LSON;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author fk7075
 * @version 1.0
 * @date 2020/11/12 10:58
 */
public class GsonSerializationScheme implements JSONSerializationScheme {

    private static final LSON lson=new LSON();

    @Override
    public String serialization(Object object) throws IOException {
        return lson.toJson(object);
    }

    @Override
    public Object deserialization(Type objectType, String objectStr) throws Exception {
        return lson.fromJson(objectType,objectStr);
    }
}
