package com.assignment.catawiki.network.interceptor

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.assignment.catawiki.network.error.NetworkNotAvailableError
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class NetworkAvailabilityInterceptor @Inject constructor(context: Context) : Interceptor {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isAvailable()) throw NetworkNotAvailableError()

        return chain.proceed(chain.request())
    }

    @Suppress("DEPRECATION")
    private fun isAvailable(): Boolean {
        return connectivityManager?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = activeNetwork ?: return false
                val actNw = getNetworkCapabilities(networkCapabilities) ?: return false
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } else {
                activeNetworkInfo?.run {
                    type == ConnectivityManager.TYPE_WIFI
                        || type == ConnectivityManager.TYPE_MOBILE
                        || type == ConnectivityManager.TYPE_ETHERNET
                } ?: false
            }
        } ?: false
    }
}
