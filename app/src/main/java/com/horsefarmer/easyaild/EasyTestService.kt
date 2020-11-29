package com.horsefarmer.easyaild

import android.util.Log
import com.horsefarmer.easyaidl.MethodExposed
import com.horsefarmer.easyaidl.EasyService

class EasyTestService : EasyService() {
    @MethodExposed
    fun requestServiceInternal() {
        Log.d("maxueming-service", "requestServiceInternal()")
    }
}
