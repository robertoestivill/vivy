package com.robertoestivill.vivy.api

import com.robertoestivill.vivy.api.model.ApiResult
import com.robertoestivill.vivy.api.model.DtoSearchResponse
import com.robertoestivill.vivy.api.model.DtoTokensResponse

interface ApiClient {

  suspend fun retrieveToken(
    username: String,
    password: String
  ): ApiResult<DtoTokensResponse>

  suspend fun retrieveDoctors(
    search: String,
    latitude: Double?,
    longitude: Double?,
    lastPageKey: String?
  ): ApiResult<DtoSearchResponse>

  suspend fun retrieveDoctorPictureUrl(
    doctorId: String
  ): String
}
