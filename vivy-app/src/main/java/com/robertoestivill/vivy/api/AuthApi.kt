package com.robertoestivill.vivy.api

import com.robertoestivill.vivy.api.model.DtoTokensResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

  @POST("/oauth/token")
  @Headers("Accept: application/json")
  @FormUrlEncoded
  fun retrieveToken(
    @Query("grant_type") grantType: String,
    @Field("username") username: String,
    @Field("password") password: String
  ): Call<DtoTokensResponse>

  companion object {
    const val BASE_URL = "https://auth.staging.vivy.com"
  }
}
