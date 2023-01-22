package com.assignment.catawiki.network.di

import android.content.Context
import com.assignment.catawiki.network.BuildConfig
import com.assignment.catawiki.network.interceptor.NetworkAvailabilityInterceptor
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Component(modules = [NetworkModule::class])
@Singleton
interface NetworkComponent {

    val httpClient: HttpClient

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): NetworkComponent
    }
}

@Module
private object NetworkModule {

    @Singleton
    @Provides
    fun networkAvailabilityInterceptor(context: Context): NetworkAvailabilityInterceptor {
        return NetworkAvailabilityInterceptor(context)
    }

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun okHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkAvailabilityInterceptor: NetworkAvailabilityInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(networkAvailabilityInterceptor)
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(httpLoggingInterceptor)
            }
            .build()
    }

    @Provides
    @Singleton
    fun httpClient(okHttpClient: OkHttpClient): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BuildConfig.HOST
                    encodedPath = "${BuildConfig.BASE_PATH}$encodedPath"
                }
            }
            engine {
                preconfigured = okHttpClient
            }

            install(ContentNegotiation) {
                val jsonConfig = Json {
                    ignoreUnknownKeys = true
                }

                json(jsonConfig)
            }
        }
    }
}
