package com.genies.avatar_api_sampleapp

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class AvatarApiSampleApplication : Application() {
    companion object {
        private var instance: Application? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}