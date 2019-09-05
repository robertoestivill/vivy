package com.robertoestivill.vivy.repository.token

import android.content.Context
import android.content.SharedPreferences
import com.robertoestivill.vivy.model.TokenModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TokenRepositoryImpl(
  context: Context,
  private val coroutineDispatcher: CoroutineDispatcher
) : TokenRepository {

  private val preferences: SharedPreferences by lazy {
    context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
  }

  override suspend fun saveToken(model: TokenModel) = withContext(coroutineDispatcher) {
    preferences.edit()
      .putString(ACCESS_TOKEN, model.accessToken)
      .putString(REFRESH_TOKEN, model.refreshToken)
      .apply()
  }

  override suspend fun retrieveToken(): TokenModel? = withContext(coroutineDispatcher) {
    val accessToken = preferences.getString(ACCESS_TOKEN, null)
    val refreshToken = preferences.getString(REFRESH_TOKEN, null)

    if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
      null
    } else {
      TokenModel(accessToken = accessToken, refreshToken = refreshToken)
    }
  }

  override suspend fun deleteToken() = withContext(coroutineDispatcher) {
    preferences.edit().clear().apply()
  }

  companion object {
    private const val PREFERENCES_FILENAME = "repository_token"

    private const val ACCESS_TOKEN = "pref_access_token"
    private const val REFRESH_TOKEN = "pref_refresh_token"
  }
}
