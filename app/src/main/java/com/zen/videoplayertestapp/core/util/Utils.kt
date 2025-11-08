package com.zen.videoplayertestapp.core.util

import android.content.Context
import com.zen.videoplayertestapp.core.helper.NetworkConnectionHelper


class Utils {
    companion object{
        fun isInternetAvailable(context: Context?): Boolean {
            return NetworkConnectionHelper.connectionStatus(context)
        }
    }

}