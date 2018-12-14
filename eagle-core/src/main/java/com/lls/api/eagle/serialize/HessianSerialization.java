package com.lls.api.eagle.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/************************************
 * HessianSerialization
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public class HessianSerialization implements Serialization {

    @Override
    public byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(outputStream);
        hessianOutput.writeObject(obj);
        hessianOutput.flush();
        return outputStream.toByteArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(inputStream);
        return (T) hessianInput.readObject();
    }

    @Override
    public byte[] batchSerialize(Object[] data) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(outputStream);
        for (Object item : data) {
            hessianOutput.writeObject(item);
        }
        hessianOutput.flush();
        return outputStream.toByteArray();
    }

    @Override
    public Object[] batchDeserialize(byte[] data, Class<?>[] classes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        HessianInput hessianInput = new HessianInput(inputStream);
        Object[] objects = new Object[classes.length];
        for (int i = 0; i < classes.length; i++) {
            objects[i] = hessianInput.readObject(classes[i]);
        }
        return objects;
    }

    @Override
    public int getSerializeVersion() {
        return SerializationVersionEnum.HESSIAN_1.getVersion();
    }

}
