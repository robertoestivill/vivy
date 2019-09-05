package com.robertoestivill.vivy.repository.location

import com.robertoestivill.vivy.model.LocationModel

interface LocationRepository {

  suspend fun retrieveLatestLocation(): LocationModel?

  suspend fun saveLocationEnabled(isLocationEnabled: Boolean)

  suspend fun isLocationEnabled(): Boolean

  suspend fun enableLocationUpdates(callback: (LocationModel) -> Unit)

  suspend fun disableLocationUpdates()
}
