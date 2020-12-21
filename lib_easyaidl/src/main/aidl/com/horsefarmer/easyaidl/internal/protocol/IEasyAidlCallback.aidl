// IEasyAidlCallback.aidl
package com.horsefarmer.easyaidl.internal.protocol;

interface IEasyAidlCallback {
    /**
     * data：传输的数据
     */
    void invoke(inout Map data);
}
