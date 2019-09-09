package com.app.baljeet.iconfinderapp.utils

import android.content.Context
import android.net.ConnectivityManager



object CommonHelper {
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
}