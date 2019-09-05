package com.robertoestivill.vivy.feature.login

sealed class LoginUiModel {

  object Loading : LoginUiModel()

  data class InitForm(
    val username: String?,
    val password: String?
  ) : LoginUiModel()

  data class Validation(
    val isUsernameValid: Boolean,
    val isPasswordValid: Boolean
  ) : LoginUiModel()

  data class Error(
    val message: String?
  ) : LoginUiModel()
}
