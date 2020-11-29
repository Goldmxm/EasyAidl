package com.horsefarmer.easyaidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.horsefarmer.easyaidl.internal.protocol.IEasyAidlBinder
import com.horsefarmer.easyaidl.internal.BinderReceiver

open class EasyService : Service() {
    override fun onBind(intent: Intent): IBinder = BinderReceiver(this)
}
