package com.robertoestivill.vivy.api.model

import com.google.gson.annotations.SerializedName

data class ApiError(

  @SerializedName("error")
  var error: String?,

  @SerializedName("error_description")
  var description: String?
)
