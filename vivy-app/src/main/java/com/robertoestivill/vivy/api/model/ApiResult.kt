package com.robertoestivill.vivy.api.model

sealed class ApiResult<T> {

  data class Success<T>(val value: T) : ApiResult<T>()

  data class Error<T>(val error: ApiError) : ApiResult<T>()

  data class Exception<T>(val exception: Throwable) : ApiResult<T>()
}
