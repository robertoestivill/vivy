package com.robertoestivill.vivy.api.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class BearerAuthorizationInterceptor(
  private val tokenProvider: suspend () -> String?
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val bearer = runBlocking { tokenProvider.invoke() }

    if (bearer.isNullOrBlank()) {
      return chain.proceed(chain.request())
    }

    val newRequest = chain.request()
      .newBuilder()
      .addHeader("Authorization", "Bearer $bearer")
      .build()

    return chain.proceed(newRequest)
  }
}
