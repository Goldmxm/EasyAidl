package com.horsefarmer.easyaidl.internal

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.annotation.WorkerThread
import com.horsefarmer.easyaidl.internal.protocol.IEasyAidlBinder
import java.lang.Exception

/**
 * Binder连接桥
 */
internal class BinderBridge(private val context: Context, packageName: String, actionName: String) {

    companion object {
        private const val TAG = "BinderBridge"
        private const val WAIT_CONNECT_TIME = 1000L       // 等待连接超时: 1s
    }

    private val connection = RemoteServiceConnection()
    private val intent = Intent().apply {
        setPackage(packageName)
        action = actionName
    }
    private var easyAidlBinder: IEasyAidlBinder? = null
    private val lock = Object()
    private var isCancelConnect = false

    @WorkerThread
    fun execute(serviceName: String, methodName: String, data: Map<Any, Any?>?): Map<Any?, Any?>? {
        if (isCancelConnect) {
            return null
        }
        val executeFun = {
            ensureConnect()
            easyAidlBinder?.invoke(serviceName, methodName, data)
        }
        return try {
            executeFun.invoke()
        } catch (e: Exception) {
            ELog.instance.e(TAG, "execute-invoke(service=$serviceName, methodName=$methodName, data=$data) error=$e")
            null
        }
    }

    fun cancelConnect(isCancelConnect: Boolean) {
        this.isCancelConnect = isCancelConnect
        connection.reset()
        context.unbindService(connection)
    }

    private fun ensureConnect() {
        if (connection.isConnect()) {
            return
        }
        connection.reset()
        var tryTime = 0
        while (context.bindService(intent, connection, Context.BIND_AUTO_CREATE) && easyAidlBinder == null && ++tryTime <= 3) {
            synchronized(lock) {
                lock.wait(WAIT_CONNECT_TIME * tryTime)
            }
        }
    }

    private inner class RemoteServiceConnection: ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            reset()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            easyAidlBinder = IEasyAidlBinder.Stub.asInterface(service)
            synchronized(lock) {
                lock.notifyAll()
            }
        }

        /**
         * 判断是否连接
         */
        fun isConnect() = easyAidlBinder?.asBinder()?.isBinderAlive == true

        /**
         * 重置连接状态
         */
        fun reset() {
            easyAidlBinder = null
        }
    }
}
