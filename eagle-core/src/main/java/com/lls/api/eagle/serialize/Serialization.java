package com.lls.api.eagle.serialize;

import java.io.IOException;

/************************************
 * Serialization
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public interface Serialization {

    byte[] serialize(Object obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException;

    byte[] batchSerialize(Object[] data) throws IOException;

    Object[] batchDeserialize(byte[] data, Class<?>[] classes) throws IOException;

    int getSerializeVersion();

}
