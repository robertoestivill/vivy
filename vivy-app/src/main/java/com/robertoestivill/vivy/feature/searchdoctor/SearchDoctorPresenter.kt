package com.robertoestivill.vivy.feature.searchdoctor

import com.robertoestivill.vivy.api.ApiClient
import com.robertoestivill.vivy.api.model.ApiError
import com.robertoestivill.vivy.api.model.ApiResult
import com.robertoestivill.vivy.api.model.DtoDoctor
import com.robertoestivill.vivy.feature.Presenter
import com.robertoestivill.vivy.feature.Route
import com.robertoestivill.vivy.feature.View
import com.robertoestivill.vivy.model.DoctorModel
import com.robertoestivill.vivy.model.LocationModel
import com.robertoestivill.vivy.repository.location.LocationRepository
import com.robertoestivill.vivy.repository.token.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchDoctorPresenter(
  private val apiClient: ApiClient,
  private val tokenRepository: TokenRepository,
  private val locationRepository: LocationRepository
) : Presenter<SearchDoctorUiEvent, SearchDoctorUiModel> {

  private var view: View<SearchDoctorUiModel>? = null

  private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
  private var ongoingSearchJob: Job? = null

  private var lastResultsPaginationKey: String? = null

  override fun onAttach(view: View<SearchDoctorUiModel>) {
    check(this.view == null) { "View already attached" }
    this.view = view
  }

  override fun onDetach() {
    check(this.view != null) { "View already detached" }
    view = null
    coroutineScope.launch {
      locationRepository.disableLocationUpdates()
    }
  }

  override fun onUiEvent(event: SearchDoctorUiEvent) {
    when (event) {
      is SearchDoctorUiEvent.PermissionsStatus -> onCheckLocationPermissions(event)
      is SearchDoctorUiEvent.SearchQueryUpdated -> triggerNewSearch(true, event.query)
      is SearchDoctorUiEvent.SearchResultsScrolled -> triggerNewSearch(false, event.query)
      is SearchDoctorUiEvent.ClearQuery -> onClearQuery()
    }
  }

  private fun onClearQuery() {
    ongoingSearchJob?.cancel()
    view?.render(SearchDoctorUiModel.ClearFields)
  }

  private fun onCheckLocationPermissions(event: SearchDoctorUiEvent.PermissionsStatus) {
    coroutineScope.launch {
      if (!locationRepository.isLocationEnabled()) {
        renderLocation(false, null)
        return@launch // user ignored location
      }

      if (!event.areLocationPermissionsGranted) {
        view?.route(Route.LocationPermissions)
        return@launch
      }

      locationRepository.enableLocationUpdates { renderLocation(true, it) }
    }
  }

  private fun renderLocation(isLocationEnabled: Boolean, newLocation: LocationModel?) {
    view?.render(SearchDoctorUiModel.LocationUpdated(isLocationEnabled, newLocation))
  }

  private fun triggerNewSearch(isNewSearch: Boolean, searchQuery: String) {
    ongoingSearchJob?.cancel()

    if (!isNewSearch && lastResultsPaginationKey.isNullOrBlank()) {
      return // Reached last page
    }

    if (searchQuery.isBlank()) {
      view?.render(SearchDoctorUiModel.ClearFields)
      return
    }

    view?.render(SearchDoctorUiModel.Loading)
    ongoingSearchJob = coroutineScope.launch {
      delay(DEBOUNCE_SEARCH_MS)
      executeSearch(isNewSearch, searchQuery)
    }
  }

  private suspend fun executeSearch(isNewSearch: Boolean, searchQuery: String) {
    val lastLocation = locationRepository.retrieveLatestLocation()
    val apiResult = apiClient.retrieveDoctors(
      search = searchQuery,
      latitude = lastLocation?.latitude,
      longitude = lastLocation?.longitude,
      lastPageKey = if (isNewSearch) null else lastResultsPaginationKey
    )
    when (apiResult) {
      is ApiResult.Error -> onApiError(apiResult.error)
      is ApiResult.Exception -> view?.render(SearchDoctorUiModel.SearchException)
      is ApiResult.Success -> {
        lastResultsPaginationKey = apiResult.value.lastPaginationKey

        val models = apiResult.value.doctors.map { mapDtoToModel(it) }
        val uiModel = SearchDoctorUiModel.SearchResults(
          isNewSearch = isNewSearch,
          doctors = models
        )
        view?.render(uiModel)
      }
    }
  }

  private suspend fun mapDtoToModel(dto: DtoDoctor) = DoctorModel(
    name = dto.name,
    address = dto.address,
    pictureUrl = apiClient.retrieveDoctorPictureUrl(dto.id),
    email = dto.email,
    phoneNumber = dto.phoneNumber,
    website = dto.website
  )

  private suspend fun onApiError(error: ApiError) {
    if (error.error == INVALID_TOKEN_ERROR_KEY) {
      tokenRepository.deleteToken()
      view?.route(Route.Login)
    } else {
      view?.render(SearchDoctorUiModel.SearchError)
    }
  }

  companion object {
    private const val DEBOUNCE_SEARCH_MS = 500L

    private const val INVALID_TOKEN_ERROR_KEY = "invalid_token"
  }
}
