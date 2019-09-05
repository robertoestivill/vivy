package com.robertoestivill.vivy.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.robertoestivill.vivy.repository.location.LocationListenerImpl
import com.robertoestivill.vivy.repository.location.LocationRepository
import com.robertoestivill.vivy.repository.location.LocationRepositoryImpl
import com.robertoestivill.vivy.repository.token.TokenRepository
import com.robertoestivill.vivy.repository.token.TokenRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

  single {
    LocationListenerImpl(
      locationProviderClient = FusedLocationProviderClient(androidContext())
    )
  }

  single<LocationRepository> {
    LocationRepositoryImpl(
      context = androidContext(),
      coroutineDispatcher = Dispatchers.Default,
      locationListener = get()
    )
  }

  single<TokenRepository> {
    TokenRepositoryImpl(
      context = androidContext(),
      coroutineDispatcher = Dispatchers.Default
    )
  }
}
