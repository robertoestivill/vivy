package com.robertoestivill.vivy.feature

interface Presenter<UiEvent, UiModel> {

  fun onAttach(view: View<UiModel>)

  fun onDetach()

  fun onUiEvent(event: UiEvent)
}
