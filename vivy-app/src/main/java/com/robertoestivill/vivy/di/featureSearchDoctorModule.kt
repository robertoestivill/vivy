package com.robertoestivill.vivy.di

import com.robertoestivill.vivy.feature.Presenter
import com.robertoestivill.vivy.feature.searchdoctor.SearchDoctorPresenter
import com.robertoestivill.vivy.feature.searchdoctor.SearchDoctorUiEvent
import com.robertoestivill.vivy.feature.searchdoctor.SearchDoctorUiModel
import org.koin.dsl.module

val featureSearchDoctorModule = module {

  factory<Presenter<SearchDoctorUiEvent, SearchDoctorUiModel>> {
    SearchDoctorPresenter(
      apiClient = get(),
      tokenRepository = get(),
      locationRepository = get()
    )
  }
}
