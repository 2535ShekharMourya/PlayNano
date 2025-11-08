package com.zen.videoplayertestapp.core.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkConnectionHelper {
    companion object {
        fun connectionStatus(context: Context?): Boolean {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            return connectivityManager?.let {
                val network = it.activeNetwork ?: return false
                val networkCapabilities = it.getNetworkCapabilities(network) ?: return false
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            } ?: false
        }
    }
}