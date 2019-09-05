package com.robertoestivill.vivy.api

import com.robertoestivill.vivy.api.model.DtoSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface VivyApi {

  @GET("/api/users/{userId}/doctors")
  @Headers("Accept: application/json")
  fun retrieveDoctors(
    @Path("userId") userId: String,
    @Query("search") search: String?,
    @Query("lat") latitude: Double?,
    @Query("lng") longitude: Double?,
    @Query("lastKey") lastPageKey: String?
  ): Call<DtoSearchResponse>

  companion object {
    const val BASE_URL = "https://api.staging.vivy.com"
  }
}
