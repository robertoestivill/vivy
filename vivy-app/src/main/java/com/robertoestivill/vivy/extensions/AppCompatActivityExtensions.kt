package com.robertoestivill.vivy.extensions

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

private const val LOCATION_PERMISSIONS_REQUEST_CODE = 1923

private val LOCATION_PERMISSIONS = arrayOf(
  Manifest.permission.ACCESS_FINE_LOCATION,
  Manifest.permission.ACCESS_COARSE_LOCATION
)

fun AppCompatActivity.areLocationPermissionsGranted() = LOCATION_PERMISSIONS
  .map { ActivityCompat.checkSelfPermission(this, it) }
  .all { it == PackageManager.PERMISSION_GRANTED }

fun AppCompatActivity.areLocationPermissionsGrantedAfterRequest(requestCode: Int) =
  LOCATION_PERMISSIONS_REQUEST_CODE == requestCode && areLocationPermissionsGranted()

fun AppCompatActivity.requestLocationPermissions() =
  ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS, LOCATION_PERMISSIONS_REQUEST_CODE)
