package com.horsefarmer.easyaidl.internal

import android.content.Context
import android.os.IBinder
import com.horsefarmer.easyaidl.MethodExposed
import com.horsefarmer.easyaidl.EasyAidlManager
import com.horsefarmer.easyaidl.internal.protocol.IEasyAidlBinder
import com.horsefarmer.easyaidl.internal.protocol.IEasyAidlCallback
import java.lang.Exception
import java.lang.reflect.Method
import java.util.*

/**
 * 服务端跨进程通信媒介类
 */
internal class BinderReceiver(var context: Context) : IEasyAidlBinder.Stub() {

    companion object {
        private const val TAG = "EasyAidlBinder"
    }

    override fun invoke(
        serviceName: String,
        methodName: String?,
        data: MutableMap<Any?, Any?>?
    ): Map<Any, Any?>? {
        if (methodName.isNullOrEmpty()) {
            return emptyMap()
        }
        val target = if (serviceName.isEmpty()) context else EasyAidlManager.getService(serviceName)
        return target?.runMethod(methodName, data)
    }

    /**
     * 执行对象中的方法
     */
    private fun Any.runMethod(methodName: String, data: MutableMap<Any?, Any?>?): Map<Any, Any?>? {
        var targetMethod: Method?
        var targetClazz: Class<*> = this.javaClass
        val hasParam = (data != null)

        // 搜索方法
        do {
            targetMethod = try {
                val method = if (hasParam) targetClazz.getDeclaredMethod(
                    methodName,
                    Map::class.java
                ) else targetClazz.getDeclaredMethod(methodName)
                val isMethodValid = (method.getAnnotation(MethodExposed::class.java) != null)
                if (isMethodValid) method else null
            } catch (e: Exception) {
                ELog.instance.e(TAG, "getDeclaredMethod error, reason = [$e]")
                null
            }
            if (targetMethod != null) {
                ELog.instance.d(TAG, "search method[$methodName] success in class[$targetClazz]")
                break
            }
            targetClazz = targetClazz.superclass
        } while (targetClazz != Objects::class.java)

        // 执行方法
        return targetMethod?.let {
            it.isAccessible = true
            val result = if (hasParam) {
                aidlCallbackConvert(data)
                it.invoke(this, data)
            } else it.invoke(this)
            if (result is Map<*, *>) result as Map<Any, Any?> else emptyMap()
        }
    }

    private fun aidlCallbackConvert(data: MutableMap<Any?, Any?>?) {
        data?.forEach { entry ->
            val value = entry.value
            if (value != null && value is IBinder) {
                data[entry.key] = IEasyAidlCallback.Stub.asInterface(value)
            }
        }
    }
}
