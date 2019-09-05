package com.robertoestivill.vivy.api.model

import com.google.gson.annotations.SerializedName

data class DtoTokensResponse(

  @SerializedName("access_token")
  val accessToken: String,

  @SerializedName("refresh_token")
  val refreshToken: String
)
