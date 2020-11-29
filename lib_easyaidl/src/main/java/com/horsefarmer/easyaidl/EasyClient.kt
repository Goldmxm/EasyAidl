package com.horsefarmer.easyaidl

import android.content.Context
import com.horsefarmer.easyaidl.internal.BinderBridge
import com.horsefarmer.easyaidl.internal.ELog
import com.horsefarmer.easyaidl.internal.protocol.IEasyAidlCallback
import java.lang.RuntimeException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Aidl客户端实例创建
 */
class EasyClient private constructor(private val context: Context, packageName: String, actionName: String) {

    companion object {
        @JvmStatic
        fun create(context: Context, packageName: String, actionName: String) = EasyClient(context, packageName, actionName)
    }

    private val binderBridge: BinderBridge =
        BinderBridge(
            context.applicationContext,
            packageName,
            actionName
        )

    /**
     * 生成执行函数
     */
    fun <T> createFun(clazz: Class<T>): T = Proxy.newProxyInstance(context.classLoader, arrayOf(clazz), EasyInvocationHandler()) as T

    /**
     * 取消连接
     */
    fun cancelConnect() {
        binderBridge.cancelConnect(true)
    }

    /**
     * 回调函数定义
     */
    abstract class EasyCallback : IEasyAidlCallback.Stub()

    /**
     * 代理类
     */
    private inner class EasyInvocationHandler : InvocationHandler {

        override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
            val methodName = method.name
            val requestAnnotation = method.getAnnotation(MethodRequest::class.java)
            requestAnnotation ?: throw RuntimeException("The method should be described by MethodRequest")
            val serviceName = requestAnnotation.serviceName
            var data: Map<Any, Any?>? = null
            if (!args.isNullOrEmpty()) {
                check(args[0] is Map<*, *> && method.genericParameterTypes.size <= 1) {
                    "the method's param should be method() or method(map)"
                }
                data = args[0] as Map<Any, Any?>
            }
            ELog.instance.d("EasyInvocationHandler", "Proxy invoke method = $methodName, args [size = ${args?.size?: 0}, value = $args]")
            return binderBridge.execute(serviceName, methodName, data)
        }
    }
}
