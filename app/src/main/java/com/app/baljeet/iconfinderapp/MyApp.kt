package com.app.baljeet.iconfinderapp

import android.app.Application
import com.github.anrwatchdog.ANRWatchDog

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        ANRWatchDog().start()
    }
}