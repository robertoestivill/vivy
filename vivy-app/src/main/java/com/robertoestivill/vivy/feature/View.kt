package com.robertoestivill.vivy.feature

interface View<UiModel> {

  fun render(model: UiModel)

  fun route(route: Route)
}
