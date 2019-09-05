package com.robertoestivill.vivy.api.model

import com.google.gson.annotations.SerializedName

data class DtoSearchResponse(

  @SerializedName("doctors")
  val doctors: List<DtoDoctor>,

  @SerializedName("lastKey")
  val lastPaginationKey: String?
)
