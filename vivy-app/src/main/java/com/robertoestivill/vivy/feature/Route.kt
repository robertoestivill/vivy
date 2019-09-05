package com.robertoestivill.vivy.feature

sealed class Route {

  object Login : Route()

  object SearchDoctor : Route()

  object LocationPermissions : Route()
}
