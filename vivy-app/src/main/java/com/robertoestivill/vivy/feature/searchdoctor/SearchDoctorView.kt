package com.robertoestivill.vivy.feature.searchdoctor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robertoestivill.vivy.BuildConfig
import com.robertoestivill.vivy.R
import com.robertoestivill.vivy.extensions.areLocationPermissionsGranted
import com.robertoestivill.vivy.feature.Presenter
import com.robertoestivill.vivy.feature.Route
import com.robertoestivill.vivy.feature.login.LoginView
import com.robertoestivill.vivy.feature.permission.PermissionView
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import com.robertoestivill.vivy.feature.View as BaseView

class SearchDoctorView : AppCompatActivity(), BaseView<SearchDoctorUiModel> {

  private val inputView by lazy { findViewById<EditText>(R.id.search_doctor_input) }
  private val clearView by lazy { findViewById<ImageView>(R.id.search_doctor_clear_input) }
  private val recyclerView by lazy { findViewById<RecyclerView>(R.id.search_doctor_recycler_view) }
  private val messageView by lazy { findViewById<TextView>(R.id.search_doctor_message) }
  private val progressView by lazy { findViewById<View>(R.id.search_doctor_progress_bar) }
  private val locationView by lazy { findViewById<TextView>(R.id.search_doctor_location) }

  private lateinit var searchDoctorsAdapter: SearchDoctorAdapter
  private lateinit var searchDoctorsScrollListener: SearchDoctorScrollListener

  private val presenter: Presenter<SearchDoctorUiEvent, SearchDoctorUiModel> by inject()

  private val picasso: Picasso by inject()

  private var textWatcher = object : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
      clearView.visibility = if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE
      presenter.onUiEvent(SearchDoctorUiEvent.SearchQueryUpdated(p0.toString()))
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_search_doctor)

    inputView.addTextChangedListener(textWatcher)
    clearView.setOnClickListener { inputView.setText("") }

    setupRecyclerView()
  }

  override fun onStart() {
    super.onStart()
    presenter.onAttach(this)
    presenter.onUiEvent(SearchDoctorUiEvent.PermissionsStatus(areLocationPermissionsGranted()))
  }

  override fun onStop() {
    presenter.onDetach()
    super.onStop()
  }

  override fun render(model: SearchDoctorUiModel) {
    when (model) {
      is SearchDoctorUiModel.LocationUpdated -> onUpdateLocationInfo(model)
      is SearchDoctorUiModel.SearchError -> showToast(R.string.search_doctor_error)
      is SearchDoctorUiModel.SearchException -> showToast(R.string.search_doctor_exception)
      is SearchDoctorUiModel.SearchResults -> onSearchResults(model)
      is SearchDoctorUiModel.Loading -> onSerachResultsLoading()
      is SearchDoctorUiModel.ClearFields -> onClearFields()
    }
  }

  private fun setupRecyclerView() {
    val layoutManager = LinearLayoutManager(this)
    val divider = DividerItemDecoration(this, layoutManager.orientation)
    searchDoctorsAdapter = SearchDoctorAdapter(this, picasso)
    searchDoctorsScrollListener = SearchDoctorScrollListener(
      adapter = searchDoctorsAdapter,
      layoutManager = layoutManager,
      triggerCallback = {
        presenter.onUiEvent(SearchDoctorUiEvent.SearchResultsScrolled(inputView.text.toString()))
      }
    )
    recyclerView.apply {
      setLayoutManager(layoutManager)
      adapter = searchDoctorsAdapter
      addOnScrollListener(searchDoctorsScrollListener)
      addItemDecoration(divider)
    }
  }

  private fun onClearFields() {
    inputView.removeTextChangedListener(textWatcher)
    inputView.setText("")
    inputView.addTextChangedListener(textWatcher)

    searchDoctorsAdapter.addItems(true, emptyList())

    progressView.visibility = View.GONE
    messageView.visibility = View.GONE
  }

  override fun route(route: Route) = when (route) {
    is Route.Login -> {
      startActivity(Intent(this, LoginView::class.java))
      finish()
    }
    is Route.LocationPermissions -> startActivity(Intent(this, PermissionView::class.java))
    else -> error("Unsupported route: $route")
  }

  private fun onSerachResultsLoading() {
    progressView.visibility = View.VISIBLE
  }

  private fun onSearchResults(model: SearchDoctorUiModel.SearchResults) {
    progressView.visibility = View.GONE
    searchDoctorsAdapter.addItems(model.isNewSearch, model.doctors)

    if (searchDoctorsAdapter.itemCount > 0) {
      messageView.visibility = View.GONE
      recyclerView.visibility = View.VISIBLE
    } else {
      messageView.visibility = View.VISIBLE
      recyclerView.visibility = View.GONE
    }
  }

  private fun showToast(@StringRes errorResId: Int) =
    Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show()

  // Just here for debug
  private fun onUpdateLocationInfo(model: SearchDoctorUiModel.LocationUpdated) {
    if (!BuildConfig.DEBUG) {
      return
    }
    val status = if (model.isEnabled) "ON" else "OFF"
    val latitude = model.location?.latitude?.toString() ?: "N/A"
    val longitude = model.location?.longitude?.toString() ?: "N/A"

    locationView.visibility = View.VISIBLE
    @SuppressLint("SetTextI18n")
    locationView.text = """
      Location: $status
      Latitude: $latitude
      Longitude: $longitude
    """.trimIndent()

    locationView.setOnClickListener { route(Route.LocationPermissions) }
  }
}
