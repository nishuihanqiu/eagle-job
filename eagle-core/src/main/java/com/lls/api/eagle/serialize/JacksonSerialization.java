package com.lls.api.eagle.serialize;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

/************************************
 * JacksonSerialization
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public class JacksonSerialization implements Serialization {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
    }

    @Override
    public byte[] serialize(Object obj) throws IOException {
        return objectMapper.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        return objectMapper.readValue(bytes, clz);
    }

    @Override
    public byte[] batchSerialize(Object[] data) throws IOException {
        return objectMapper.writeValueAsBytes(data);
    }

    @Override
    public Object[] batchDeserialize(byte[] data, Class<?>[] classes) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, classes);
        return objectMapper.readValue(data, javaType);
    }

    @Override
    public int getSerializeVersion() {
        return SerializationVersionEnum.JACK_SON.getVersion();
    }
}
