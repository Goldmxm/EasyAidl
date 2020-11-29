package com.horsefarmer.easyaidl.internal

import android.util.Log
import androidx.annotation.IntDef

/**
 * 日志打印
 */
internal class ELog {
    companion object {
        private const val TAG_PREFIX = "EasyAidl_"
        private var logLevel = LogLevel.NONE

        @JvmStatic
        val instance: ELog by lazy { ELog() }

        fun setLogLevel(@LogLevel logLevel: Int) {
            Companion.logLevel = logLevel
        }
    }

    fun d(tag: String = "", msg: String) {
        if (logLevel <= LogLevel.DEBUG) {
            Log.d(TAG_PREFIX + tag, msg)
        }
    }

    fun e(tag: String = "", msg: String, throwable: Throwable? = null) {
        if (logLevel <= LogLevel.ERROR) {
            Log.e(TAG_PREFIX + tag, msg + throwable?.let { "\n" + Log.getStackTraceString(throwable) })
        }
    }
}

/**
 * 日志等级定义
 */
@IntDef(value = [LogLevel.NONE, LogLevel.VERBOSE, LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARN, LogLevel.ERROR, LogLevel.ASSERT])
annotation class LogLevel {
    companion object {
        const val VERBOSE = 1
        const val DEBUG = 2
        const val INFO = 3
        const val WARN = 4
        const val ERROR = 5
        const val ASSERT = 6
        const val NONE = 7
    }
}
