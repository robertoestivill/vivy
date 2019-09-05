package com.robertoestivill.vivy.api.interceptors

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthorizationInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val credentials = "$HTTP_BASIC_AUTH_USERNAME:$HTTP_BASIC_AUTH_PASSWORD".toByteArray()
    val headerValue = "Basic " + Base64.encodeToString(credentials, Base64.NO_WRAP)

    val newRequest = chain
      .request()
      .newBuilder()
      .addHeader("Authorization", headerValue)
      .build()

    return chain.proceed(newRequest)
  }

  companion object {
    private const val HTTP_BASIC_AUTH_USERNAME = "REDACTED"
    private const val HTTP_BASIC_AUTH_PASSWORD = "REDACTED"
  }
}
