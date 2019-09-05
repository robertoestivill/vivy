package com.robertoestivill.vivy

import android.app.Application
import com.robertoestivill.vivy.di.featureLoginModule
import com.robertoestivill.vivy.di.featureSearchDoctorModule
import com.robertoestivill.vivy.di.networkModule
import com.robertoestivill.vivy.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VivyApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@VivyApplication)
      modules(
        listOf(
          repositoryModule,
          networkModule,
          featureLoginModule,
          featureSearchDoctorModule
        )
      )
    }
  }
}
