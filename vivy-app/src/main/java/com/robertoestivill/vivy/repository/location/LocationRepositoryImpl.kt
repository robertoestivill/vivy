package com.robertoestivill.vivy.repository.location

import android.content.Context
import com.robertoestivill.vivy.model.LocationModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
  context: Context,
  private val coroutineDispatcher: CoroutineDispatcher,
  private val locationListener: LocationListenerImpl
) : LocationRepository {

  private val preferences by lazy {
    context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
  }

  // Could easily be persisted on the above preferences
  private var lastLocation: LocationModel? = null

  override suspend fun retrieveLatestLocation(): LocationModel? = withContext(coroutineDispatcher) {
    lastLocation
  }

  override suspend fun saveLocationEnabled(isLocationEnabled: Boolean) =
    withContext(coroutineDispatcher) {
      preferences.edit().putBoolean(LOCATION_ENABLED, isLocationEnabled).apply()
    }

  override suspend fun isLocationEnabled(): Boolean = withContext(coroutineDispatcher) {
    preferences.getBoolean(LOCATION_ENABLED, true)
  }

  override suspend fun enableLocationUpdates(callback: (LocationModel) -> Unit) {
    locationListener.enableLocationUpdates {
      lastLocation = it
      callback.invoke(it)
    }
  }

  override suspend fun disableLocationUpdates() {
    locationListener.disableLocationUpdates()
  }

  companion object {
    private const val PREFS_FILENAME = "repository_location"

    private const val LOCATION_ENABLED = "pref_location_enabled"
  }
}
