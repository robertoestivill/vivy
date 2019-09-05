package com.robertoestivill.vivy.feature.searchdoctor

import com.robertoestivill.vivy.model.DoctorModel
import com.robertoestivill.vivy.model.LocationModel

sealed class SearchDoctorUiModel {

  object Loading : SearchDoctorUiModel()

  object SearchError : SearchDoctorUiModel()

  object SearchException : SearchDoctorUiModel()

  object ClearFields : SearchDoctorUiModel()

  data class LocationUpdated(
    val isEnabled: Boolean,
    val location: LocationModel?
  ) : SearchDoctorUiModel()

  data class SearchResults(
    val isNewSearch: Boolean,
    val doctors: List<DoctorModel>
  ) : SearchDoctorUiModel()
}
