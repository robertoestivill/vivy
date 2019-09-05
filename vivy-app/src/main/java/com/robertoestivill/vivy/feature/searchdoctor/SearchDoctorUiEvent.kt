package com.robertoestivill.vivy.feature.searchdoctor

sealed class SearchDoctorUiEvent {

  object ClearQuery : SearchDoctorUiEvent()

  data class PermissionsStatus(
    val areLocationPermissionsGranted: Boolean
  ) : SearchDoctorUiEvent()

  data class SearchQueryUpdated(
    val query: String
  ) : SearchDoctorUiEvent()

  data class SearchResultsScrolled(
    val query: String
  ) : SearchDoctorUiEvent()
}
