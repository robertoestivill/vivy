package com.robertoestivill.vivy.di

import com.jakewharton.picasso.OkHttp3Downloader
import com.robertoestivill.vivy.BuildConfig
import com.robertoestivill.vivy.api.ApiClient
import com.robertoestivill.vivy.api.ApiClientImpl
import com.robertoestivill.vivy.api.interceptors.BearerAuthorizationInterceptor
import com.robertoestivill.vivy.repository.token.TokenRepository
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {

  val defaultTimeoutSeconds = 30L
  val diskCacheMaxSizeBytes = 50L * 1025L

  single {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }

    val cache = Cache(androidContext().cacheDir, diskCacheMaxSizeBytes)

    OkHttpClient.Builder()
      .readTimeout(defaultTimeoutSeconds, TimeUnit.SECONDS)
      .connectTimeout(defaultTimeoutSeconds, TimeUnit.SECONDS)
      .writeTimeout(defaultTimeoutSeconds, TimeUnit.SECONDS)
      .addNetworkInterceptor(loggingInterceptor)
      .cache(cache)
      .build()
  }

  single {
    val authInterceptor = BearerAuthorizationInterceptor {
      get<TokenRepository>().retrieveToken()?.accessToken
    }

    val okHttpClient = get<OkHttpClient>()
      .newBuilder()
      .addInterceptor(authInterceptor)
      .build()

    val okHttpDownloader = OkHttp3Downloader(okHttpClient)

    Picasso.Builder(androidContext())
      .indicatorsEnabled(BuildConfig.DEBUG)
      .loggingEnabled(BuildConfig.DEBUG)
      .memoryCache(LruCache(androidContext()))
      .downloader(okHttpDownloader)
      .build()
  }

  single<ApiClient> {
    ApiClientImpl(
      okHttpClient = get(),
      coroutineDispatcher = Dispatchers.IO,
      tokenProvider = { get<TokenRepository>().retrieveToken()?.accessToken }
    )
  }
}
