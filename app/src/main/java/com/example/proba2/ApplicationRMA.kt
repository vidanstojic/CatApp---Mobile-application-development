package com.example.proba2

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationRMA : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("Test", "App:onCreate()")
    }

}