package cn.rbf.serializable.implement;

import cn.rbf.serializable.SerializationScheme;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;

/**
 * @author fk7075
 * @version 1.0
 * @date 2020/11/12 10:49
 */
public class JDKSerializationScheme implements SerializationScheme {

    @Override
    public String serialization(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOut=new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteArrayOut);
        objOut.writeObject(object);
        String objectStr = byteArrayOut.toString("ISO-8859-1");
        objOut.close();
        byteArrayOut.close();
        return objectStr;
    }

    @Override
    public Object deserialization(Type objectType, String objectStr) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object =  objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }

}
