package com.example.testovoe14

import android.app.Application
import com.onesignal.OneSignal

const val ONESIGNAL_APP_ID = "d0610706-4813-4c88-bd5e-544e6a47b5c9"

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}