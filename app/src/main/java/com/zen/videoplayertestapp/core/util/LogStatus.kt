package com.brochill.minismodule.util

import android.util.Log

class LogStatus {
    companion object{
        fun Any?.logs() = Log.e("azad",":-: ${this ?: "null"} :-:")
        fun Any?.logs1() = Log.e("shekhar",":-: ${this ?: "null"} :-:")
        fun Any?.mLog() = Log.e("LOG_STATUS",":-: ${this ?: "null"} :-:")


    }
}