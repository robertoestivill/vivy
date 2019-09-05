package com.robertoestivill.vivy.feature.searchdoctor

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchDoctorScrollListener(
  private val adapter: SearchDoctorAdapter,
  private val layoutManager: LinearLayoutManager,
  private val triggerCallback: () -> Unit
) : RecyclerView.OnScrollListener() {

  private var lastTotalItems = -1
  private var lastVisiblePosition = -1

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    val newTotalItems = adapter.itemCount
    val newLastVisiblePosition = layoutManager.findLastVisibleItemPosition()

    if (newTotalItems == lastTotalItems && lastVisiblePosition == newLastVisiblePosition) {
      return // avoid scroll callback abuse
    }

    lastTotalItems = newTotalItems
    lastVisiblePosition = newLastVisiblePosition

    if (lastTotalItems < (lastVisiblePosition + ELEMENTS_UNTIL_LAST_POSITION)) {
      triggerCallback.invoke()
      Log.d("SCROLLVIVY", "lastVisible: $lastVisiblePosition | total: $lastTotalItems")
    }
  }

  companion object {
    private const val ELEMENTS_UNTIL_LAST_POSITION = 4
  }
}
