package com.lls.api.eagle.serialize;

import java.io.IOException;

/************************************
 * ProtobufSerialization
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public class ProtobufSerialization implements Serialization {

    @Override
    public byte[] serialize(Object obj) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        return null;
    }

    @Override
    public byte[] batchSerialize(Object[] data) throws IOException {
        return new byte[0];
    }

    @Override
    public Object[] batchDeserialize(byte[] data, Class<?>[] classes) throws IOException {
        return new Object[0];
    }

    @Override
    public int getSerializeVersion() {
        return 0;
    }

}
