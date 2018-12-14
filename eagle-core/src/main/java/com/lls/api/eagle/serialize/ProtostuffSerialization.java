package com.lls.api.eagle.serialize;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.IOException;

/************************************
 * ProtostuffSerialization
 *
 * 在使用 Protostuff 的时候会遇到一些无法直接序列化/反序列化的对象，比如Map、List等。
 * 在序列化这些无法直接序列化/反序列化的对象时，可以引入一个包装类来吧这些数据包装一下，然后对这个包装类进行序列化/反序列化。
 *
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public class ProtostuffSerialization implements Serialization {

    private static Objenesis objenesis = new ObjenesisStd(true);

    @SuppressWarnings("unchecked")
    @Override
    public byte[] serialize(Object obj) throws IOException {
        Schema schema = RuntimeSchema.getSchema(obj.getClass());
        return ProtostuffIOUtil.toByteArray(obj, schema, LinkedBuffer.allocate());
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        Schema<T> schema = RuntimeSchema.getSchema(clz);
//        T obj = objenesis.newInstance(clz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    @Override
    public byte[] batchSerialize(Object[] data) throws IOException {
        SerializeDeserializeWrapper wrapper = SerializeDeserializeWrapper.build(data);
        return this.serialize(wrapper);
    }

    @Override
    public Object[] batchDeserialize(byte[] data, Class<?>[] classes) throws IOException {
        SerializeDeserializeWrapper wrapper = this.deserialize(data, SerializeDeserializeWrapper.class);
        return wrapper.getData();
    }

    @Override
    public int getSerializeVersion() {
        return 0;
    }

    private static class SerializeDeserializeWrapper {

        private Object[] data;

        private static SerializeDeserializeWrapper build(Object[] data) {
            SerializeDeserializeWrapper wrapper = new SerializeDeserializeWrapper();
            wrapper.data = data;
            return wrapper;
        }

        private Object[] getData() {
            return data;
        }
    }

}
