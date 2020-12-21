// IEasyAidlBinder.aidl
package com.horsefarmer.easyaidl.internal.protocol;

import com.horsefarmer.easyaidl.internal.protocol.IEasyAidlCallback;

interface IEasyAidlBinder {
    /**
     * data：传输的数据
     */
    Map invoke(String serviceName, String methodName, inout Map data);
}
