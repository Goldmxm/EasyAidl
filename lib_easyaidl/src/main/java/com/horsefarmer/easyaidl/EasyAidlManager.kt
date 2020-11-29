package com.horsefarmer.easyaidl

import android.content.Context
import android.os.Binder
import com.horsefarmer.easyaidl.internal.ELog
import com.horsefarmer.easyaidl.internal.LogLevel
import java.util.concurrent.ConcurrentHashMap

/**
 * Aidl服务绑定管理
 */
object EasyAidlManager {
    private val serviceMap = ConcurrentHashMap<String, Any>()

    /**
     * 注册服务
     */
    @JvmStatic
    fun registerService(serviceName: String, factory: (() -> Any)) {
        serviceMap[serviceName] = factory.invoke()
    }

    /**
     * 解除服务
     */
    @JvmStatic
    fun unRegisterService(serviceName: String) {
        serviceMap.remove(serviceName)
    }

    /**
     * 获取调用当前函数的来源应用包名，一般用于根据包名判断调用方权限
     */
    @JvmStatic
    fun getCallingPackageName(context: Context): String? {
        val callingUid = Binder.getCallingUid()
        val packageNameList = context.packageManager.getPackagesForUid(callingUid)
        return if (!packageNameList.isNullOrEmpty()) {
            packageNameList[0]
        } else null
    }

    /**
     * 设置日志级别
     */
    @JvmStatic
    fun setLogLevel(@LogLevel logLevel: Int) {
        ELog.setLogLevel(logLevel)
    }

    internal fun getService(serviceName: String): Any? = serviceMap[serviceName]
}
