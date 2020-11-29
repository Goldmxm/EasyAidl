-keep class com.horsefarmer.easyaidl.MethodExposed
-keep class com.horsefarmer.easyaidl.MethodRequest

-keepclasseswithmembers class * {
    @com.horsefarmer.easyaidl.MethodExposed <methods>;
}
-keepclasseswithmembers class * {
    @com.horsefarmer.easyaidl.MethodRequest <methods>;
}
