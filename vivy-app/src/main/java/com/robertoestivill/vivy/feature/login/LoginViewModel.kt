package com.robertoestivill.vivy.feature.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robertoestivill.vivy.api.ApiClient
import com.robertoestivill.vivy.api.model.ApiResult
import com.robertoestivill.vivy.feature.Route
import com.robertoestivill.vivy.model.TokenModel
import com.robertoestivill.vivy.repository.token.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
  private val apiClient: ApiClient,
  private val tokenRepository: TokenRepository
) : ViewModel() {

  private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
  private var validationJob: Job? = null

  val uiModel = MutableLiveData<LoginUiModel>()
  val router = MutableLiveData<Route>()

  init {
    // Perfect place to put the testing email / password for speedy development
    uiModel.value = LoginUiModel.InitForm("", "")
  }

  override fun onCleared() {
    coroutineScope.cancel()
    super.onCleared()
  }

  fun onUiEvent(event: LoginUiEvent) {
    coroutineScope.launch {
      when (event) {
        is LoginUiEvent.FormUpdated -> onFormUpdate(event)
        is LoginUiEvent.FormSubmitted -> onFormSubmitted(event)
      }
    }
  }

  private suspend fun onFormSubmitted(uiEvent: LoginUiEvent.FormSubmitted) {
    uiModel.value = LoginUiModel.Loading
    val apiResult = apiClient.retrieveToken(
      username = uiEvent.username,
      password = uiEvent.password
    )

    when (apiResult) {
      is ApiResult.Error -> uiModel.value = LoginUiModel.Error(apiResult.error.description)
      is ApiResult.Exception -> uiModel.value = LoginUiModel.Error(apiResult.exception.message)
      is ApiResult.Success -> {
        val model = TokenModel(
          accessToken = apiResult.value.accessToken,
          refreshToken = apiResult.value.refreshToken
        )
        tokenRepository.saveToken(model)
        router.value = Route.SearchDoctor
      }
    }
  }

  private suspend fun onFormUpdate(uiEvent: LoginUiEvent.FormUpdated) {
    validationJob?.cancel()
    delay(VALIDATION_DELAY_MS)
    val isUsernameValid = isEmailValid(uiEvent.username)
    val isPasswordValid = isPasswordValid(uiEvent.password)
    uiModel.value = LoginUiModel.Validation(isUsernameValid, isPasswordValid)
  }

  private fun isEmailValid(email: String?) =
    email?.let { Patterns.EMAIL_ADDRESS.matcher(email).matches() } ?: false

  private fun isPasswordValid(password: String?) =
    password?.let { password.length >= PASSWORD_MIN_LENGTH } ?: false

  companion object {
    private const val VALIDATION_DELAY_MS = 300L
    private const val PASSWORD_MIN_LENGTH = 6
  }
}
