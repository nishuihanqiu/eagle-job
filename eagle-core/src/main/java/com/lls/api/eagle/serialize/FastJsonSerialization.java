package com.lls.api.eagle.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.util.List;

/************************************
 * FastJsonSerialization
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public class FastJsonSerialization implements Serialization {

    @Override
    public byte[] serialize(Object obj) throws IOException {
        SerializeWriter writer = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(writer);
        serializer.config(SerializerFeature.WriteEnumUsingToString, true);
        serializer.config(SerializerFeature.WriteClassName, true);
        serializer.write(obj);
        return writer.toBytes("UTF-8");
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        return JSONObject.parseObject(bytes, clz);
    }

    @Override
    public byte[] batchSerialize(Object[] data) throws IOException {
        return serialize(data);
    }

    @Override
    public Object[] batchDeserialize(byte[] data, Class<?>[] classes) throws IOException {
        List<Object> objects = JSON.parseArray(new String(data), classes);
        if (objects == null) {
            return null;
        }
        return objects.toArray();
    }

    @Override
    public int getSerializeVersion() {
        return SerializationVersionEnum.FAST_JSON.getVersion();
    }

}
