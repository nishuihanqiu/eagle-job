package com.lls.api.eagle.serialize;

import java.io.IOException;

/************************************
 * SerailizationContext
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public class SerailizationContext implements Serialization {

    private Serialization serialization;
    private byte[] bytes;

    public SerailizationContext(Serialization serialization, byte[] bytes) {
        this.serialization = serialization;
        this.bytes = bytes;
    }


    @Override
    public byte[] serialize(Object obj) throws IOException {
        return serialization.serialize(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        return serialization.deserialize(bytes, clz);
    }

    public <T> T deserialize(Class<T> clz) throws IOException {
        return serialization.deserialize(bytes, clz);
    }

    @Override
    public byte[] batchSerialize(Object[] data) throws IOException {
        return serialization.batchSerialize(data);
    }

    @Override
    public Object[] batchDeserialize(byte[] data, Class<?>[] classes) throws IOException {
        return serialization.batchDeserialize(data, classes);
    }

    @Override
    public int getSerializeVersion() {
        return serialization.getSerializeVersion();
    }
}
