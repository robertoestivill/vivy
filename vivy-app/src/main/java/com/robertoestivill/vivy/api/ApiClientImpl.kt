package com.robertoestivill.vivy.api

import com.google.gson.Gson
import com.robertoestivill.vivy.api.interceptors.BasicAuthorizationInterceptor
import com.robertoestivill.vivy.api.interceptors.BearerAuthorizationInterceptor
import com.robertoestivill.vivy.api.model.ApiError
import com.robertoestivill.vivy.api.model.ApiResult
import com.robertoestivill.vivy.api.model.DtoSearchResponse
import com.robertoestivill.vivy.api.model.DtoTokensResponse
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClientImpl(
  private val okHttpClient: OkHttpClient,
  private val coroutineDispatcher: CoroutineDispatcher,
  private val tokenProvider: suspend () -> String?
) : ApiClient {

  override suspend fun retrieveDoctorPictureUrl(doctorId: String) =
    "${VivyApi.BASE_URL}/api/doctors/$doctorId/keys/profilepictures"

  private val gson = Gson()

  private val gsonConverter by lazy {
    GsonConverterFactory.create(gson)
  }

  private val authApi by lazy {
    val authInterceptor = BasicAuthorizationInterceptor()

    val okHttpClient = okHttpClient
      .newBuilder()
      .addInterceptor(authInterceptor)
      .build()

    Retrofit.Builder()
      .baseUrl(AuthApi.BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(gsonConverter)
      .build()
      .create(AuthApi::class.java)
  }

  private val vivyApi by lazy {
    val authInterceptor =
      BearerAuthorizationInterceptor(tokenProvider)

    val okHttpClient = okHttpClient
      .newBuilder()
      .addInterceptor(authInterceptor)
      .build()

    Retrofit.Builder()
      .baseUrl(VivyApi.BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(gsonConverter)
      .build()
      .create(VivyApi::class.java)
  }

  override suspend fun retrieveToken(
    username: String,
    password: String
  ): ApiResult<DtoTokensResponse> = invoke(
    authApi.retrieveToken(
      grantType = "password",
      username = username,
      password = password
    )
  )

  override suspend fun retrieveDoctors(
    search: String,
    latitude: Double?,
    longitude: Double?,
    lastPageKey: String?
  ): ApiResult<DtoSearchResponse> = invoke(
    vivyApi.retrieveDoctors(
      userId = "me",
      search = search,
      latitude = latitude,
      longitude = longitude,
      lastPageKey = lastPageKey
    )
  )

  private suspend fun <T> invoke(call: Call<T>): ApiResult<T> {
    return withContext(coroutineDispatcher) { call.toApiResult() }
  }

  // Safely convert a Retrofit call to an ApiResult
  private fun <T> Call<T>.toApiResult(): ApiResult<T> = try {
    val response = execute()
    if (response.isSuccessful) {
      val successBody = response.body()

      @Suppress("UNCHECKED_CAST")
      if (successBody == null) ApiResult.Success(Unit as T) else ApiResult.Success(successBody)
    } else {
      val errorBody =
        response.errorBody()?.charStream() ?: error("Error response has no body")
      val apiError = gson.fromJson(errorBody, ApiError::class.java)
      ApiResult.Error(apiError)
    }
  } catch (ioException: IOException) {
    ApiResult.Exception(ioException)
  }
}
