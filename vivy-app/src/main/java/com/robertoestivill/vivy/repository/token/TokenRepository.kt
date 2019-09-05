package com.robertoestivill.vivy.repository.token

import com.robertoestivill.vivy.model.TokenModel

interface TokenRepository {

  suspend fun saveToken(model: TokenModel)

  suspend fun retrieveToken(): TokenModel?

  suspend fun deleteToken()
}
