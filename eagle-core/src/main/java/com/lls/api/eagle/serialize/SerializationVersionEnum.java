package com.lls.api.eagle.serialize;

/************************************
 * SerializationVersionEnum
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public enum SerializationVersionEnum {
    HESSIAN_1(0),
    HESSIAN_2(1),
    JACK_SON(2),
    FAST_JSON(3),
    PROTOBUF(4),
    PROTOSTUFF(5)
    ;
    private int version;

    SerializationVersionEnum(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
