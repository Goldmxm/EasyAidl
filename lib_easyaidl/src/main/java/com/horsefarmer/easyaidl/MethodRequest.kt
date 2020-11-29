package com.horsefarmer.easyaidl

/**
 * 客户端注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MethodRequest(val serviceName: String = "")
