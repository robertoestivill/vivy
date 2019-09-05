package com.robertoestivill.vivy.repository.location

import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.robertoestivill.vivy.model.LocationModel

class LocationListenerImpl(
  private val locationProviderClient: FusedLocationProviderClient
) : LocationListener {

  private var locationCallback: (LocationModel) -> Unit = {}

  private val fusedLocationRequest = LocationRequest().apply {
    interval = LOCATION_UPDATE_INTERVAL
    priority = LOCATION_UPDATE_PRIORITY
  }

  private val fusedLocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult?) {
      super.onLocationResult(locationResult)
      locationResult?.let { updateLocation(it) }
    }
  }

  override suspend fun enableLocationUpdates(callback: (LocationModel) -> Unit) {
    locationCallback = callback
    locationProviderClient.requestLocationUpdates(
      fusedLocationRequest,
      fusedLocationCallback,
      Looper.getMainLooper()
    )
  }

  override suspend fun disableLocationUpdates() {
    locationProviderClient.removeLocationUpdates(fusedLocationCallback)
  }

  private fun updateLocation(locationResult: LocationResult) {
    locationResult.lastLocation?.let {
      val newLocation = LocationModel(
        latitude = it.latitude,
        longitude = it.longitude
      )
      locationCallback.invoke(newLocation)
    }
  }

  companion object {
    private const val LOCATION_UPDATE_INTERVAL = 5000L
    private const val LOCATION_UPDATE_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY
  }
}
