package com.robertoestivill.vivy.feature.login

sealed class LoginUiEvent {

  data class FormUpdated(
    val username: String?,
    val password: String?
  ) : LoginUiEvent()

  data class FormSubmitted(
    val username: String,
    val password: String
  ) : LoginUiEvent()
}
