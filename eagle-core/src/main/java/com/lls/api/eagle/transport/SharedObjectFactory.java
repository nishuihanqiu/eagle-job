package com.lls.api.eagle.transport;

/************************************
 * SharedObjectFactory
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public interface SharedObjectFactory<T> {

    /**
     * 创建对象
     *
     * @return
     */
    T makeObject();

    /**
     * 重建对象
     * @param obj
     * @return
     */
    boolean rebuildObject(T obj);

}
