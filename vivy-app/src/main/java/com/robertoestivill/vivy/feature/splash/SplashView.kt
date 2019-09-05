package com.robertoestivill.vivy.feature.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robertoestivill.vivy.feature.login.LoginView
import com.robertoestivill.vivy.feature.searchdoctor.SearchDoctorView
import com.robertoestivill.vivy.repository.token.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashView : AppCompatActivity() {

  private val tokenRepository: TokenRepository by inject()
  private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    coroutineScope.launch {
      val nextActivity = when (tokenRepository.retrieveToken()) {
        null -> LoginView::class.java
        else -> SearchDoctorView::class.java
      }
      startActivity(Intent(this@SplashView, nextActivity))
      finish()
    }
  }

  override fun onDestroy() {
    coroutineScope.cancel()
    super.onDestroy()
  }
}
