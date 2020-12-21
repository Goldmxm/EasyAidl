package com.horsefarmer.easyaild

import android.content.Context
import android.util.Log
import com.horsefarmer.easyaidl.MethodExposed
import com.horsefarmer.easyaidl.EasyAidlManager
import com.horsefarmer.easyaidl.internal.protocol.IEasyAidlCallback

class TestAidlService constructor(val context: Context){

    companion object {
        const val SERVICE_NAME = "TestAidlService"
    }

    @MethodExposed
    fun print() {
        Log.d("maxueming-service", "getCallingPackageName=${EasyAidlManager.getCallingPackageName(context)}-print()")
    }

    @MethodExposed
    fun print(data: Map<Any, Any>) {
        Log.d("maxueming-service", "print($data)")
    }

    @MethodExposed
    fun obtainMap(): Map<Any, Any> {
        Log.d("maxueming-service", "obtainMap()")
        return mapOf("key" to "value")
    }

    @MethodExposed
    fun transferCallback(data: Map<Any, Any>) {
        Log.d("maxueming-service", "transferCallback($data)")
        (data["callback"] as? IEasyAidlCallback)?.invoke(mapOf("key" to "value"))
    }
}
