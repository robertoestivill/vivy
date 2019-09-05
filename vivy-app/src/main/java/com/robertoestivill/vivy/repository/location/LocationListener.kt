package com.robertoestivill.vivy.repository.location

import com.robertoestivill.vivy.model.LocationModel

interface LocationListener {

  suspend fun enableLocationUpdates(callback: (LocationModel) -> Unit)

  suspend fun disableLocationUpdates()
}
