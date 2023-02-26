package com.yerastov.assignment.pokedex.core.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.net.HttpURLConnection
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

const val ERROR_MESSAGE_NETWORK_NOT_AVAILABLE = "network is not available"

class NetworkAvailabilityInterceptor @Inject constructor(context: Context) : Interceptor {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isAvailable()) {
            return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(HttpURLConnection.HTTP_BAD_REQUEST)
                .message(ERROR_MESSAGE_NETWORK_NOT_AVAILABLE)
                .body(ERROR_MESSAGE_NETWORK_NOT_AVAILABLE.toResponseBody(null))
                .build()
        }

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
