package com.robertoestivill.vivy.api.model

import com.google.gson.annotations.SerializedName

data class DtoDoctor(

  @SerializedName("id")
  val id: String,

  @SerializedName("name")
  val name: String,

  @SerializedName("address")
  val address: String?,

  @SerializedName("website")
  val website: String?,

  @SerializedName("phoneNumber")
  val phoneNumber: String?,

  @SerializedName("email")
  val email: String?
)
