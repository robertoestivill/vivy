package com.robertoestivill.vivy.feature.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.robertoestivill.vivy.R
import com.robertoestivill.vivy.extensions.onTextChanged
import com.robertoestivill.vivy.feature.Route
import com.robertoestivill.vivy.feature.searchdoctor.SearchDoctorView
import org.koin.android.viewmodel.ext.android.viewModel
import com.robertoestivill.vivy.feature.View as BaseView

class LoginView : AppCompatActivity(), BaseView<LoginUiModel> {

  private val usernameView by lazy { findViewById<EditText>(R.id.login_username) }
  private val passwordView by lazy { findViewById<EditText>(R.id.login_password) }
  private val progressView by lazy { findViewById<View>(R.id.login_progress_bar) }
  private val errorView by lazy { findViewById<TextView>(R.id.login_error) }
  private val buttonView by lazy { findViewById<View>(R.id.login_button) }

  private val colorTextNormal by lazy { getColor(R.color.vivyGrey) }
  private val colorTextError by lazy { getColor(R.color.vivyRed) }

  private val loginViewModel: LoginViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_login)

    usernameView.onTextChanged { triggerFormUpdated() }
    passwordView.onTextChanged { triggerFormUpdated() }
    buttonView.setOnClickListener { triggerFormSubmitted() }

    loginViewModel.uiModel.observe(this, Observer { render(it) })
    loginViewModel.router.observe(this, Observer { route(it) })
  }

  override fun render(model: LoginUiModel) = when (model) {
    is LoginUiModel.Loading -> renderLoading()
    is LoginUiModel.InitForm -> renderInitForm(model)
    is LoginUiModel.Validation -> renderValidation(model)
    is LoginUiModel.Error -> renderError(model)
  }

  override fun route(route: Route) = when (route) {
    Route.SearchDoctor -> {
      finish()
      startActivity(Intent(this, SearchDoctorView::class.java))
    }
    else -> error("Unsupported route $route")
  }

  private fun renderLoading() {
    usernameView.isEnabled = false
    passwordView.isEnabled = false
    buttonView.isEnabled = false

    progressView.visibility = View.VISIBLE
    errorView.visibility = View.INVISIBLE
  }

  private fun renderInitForm(uiModel: LoginUiModel.InitForm) {
    usernameView.setText(uiModel.username ?: "")
    passwordView.setText(uiModel.password ?: "")
  }

  private fun renderValidation(uiModel: LoginUiModel.Validation) {
    usernameView.setTextColor(if (uiModel.isUsernameValid) colorTextNormal else colorTextError)
    passwordView.setTextColor(if (uiModel.isPasswordValid) colorTextNormal else colorTextError)
  }

  private fun renderError(uiModel: LoginUiModel.Error) {
    usernameView.isEnabled = true
    passwordView.isEnabled = true
    buttonView.isEnabled = true

    progressView.visibility = View.INVISIBLE
    errorView.visibility = View.VISIBLE
    errorView.text = uiModel.message ?: getString(R.string.login_error)
  }

  private fun triggerFormUpdated() {
    val uiEvent = LoginUiEvent.FormUpdated(
      username = usernameView.text.toString(),
      password = passwordView.text.toString()
    )
    loginViewModel.onUiEvent(uiEvent)
  }

  private fun triggerFormSubmitted() {
    val uiEvent = LoginUiEvent.FormSubmitted(
      username = usernameView.text.toString(),
      password = passwordView.text.toString()
    )
    loginViewModel.onUiEvent(uiEvent)
  }
}
