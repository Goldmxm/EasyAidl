package com.horsefarmer.easyaild

import android.app.Application
import com.horsefarmer.easyaidl.EasyAidlManager

class EasyAidlApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EasyAidlManager.registerService(TestAidlService.SERVICE_NAME) {
            TestAidlService(this)
        }
    }
}
