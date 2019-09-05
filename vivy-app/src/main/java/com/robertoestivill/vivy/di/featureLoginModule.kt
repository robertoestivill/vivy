package com.robertoestivill.vivy.di

import com.robertoestivill.vivy.feature.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureLoginModule = module {

  viewModel {
    LoginViewModel(
      apiClient = get(),
      tokenRepository = get()
    )
  }
}
