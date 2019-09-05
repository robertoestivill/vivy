package com.robertoestivill.vivy.feature.permission

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.robertoestivill.vivy.R
import com.robertoestivill.vivy.extensions.areLocationPermissionsGranted
import com.robertoestivill.vivy.extensions.areLocationPermissionsGrantedAfterRequest
import com.robertoestivill.vivy.extensions.requestLocationPermissions
import com.robertoestivill.vivy.repository.location.LocationRepository
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class PermissionView : AppCompatActivity() {

  private val containerView by lazy { findViewById<View>(R.id.permissions_container) }
  private val ignoreView by lazy { findViewById<View>(R.id.permissions_ignore) }
  private val grantView by lazy { findViewById<View>(R.id.permissions_grant) }

  private val locationRepository: LocationRepository by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.view_permissions)
    grantView.setOnClickListener { requestLocationPermissions() }
    ignoreView.setOnClickListener {
      toggleLocation(false)
      finish()
    }
  }

  override fun onStart() {
    super.onStart()
    if (areLocationPermissionsGranted()) {
      toggleLocation(true)
      finish()
    } else {
      requestLocationPermissions()
    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    if (areLocationPermissionsGrantedAfterRequest(requestCode)) {
      toggleLocation(true)
      finish()
    } else {
      containerView.visibility = View.VISIBLE
    }
  }

  private fun toggleLocation(isEnabled: Boolean) = runBlocking {
    locationRepository.saveLocationEnabled(isEnabled)
  }
}
