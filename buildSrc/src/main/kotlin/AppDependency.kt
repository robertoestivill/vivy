object AppDependency {
  const val androidLiveData = "android.arch.lifecycle:livedata:${Version.androidLiveData}"
  const val androidViewModel = "android.arch.lifecycle:viewmodel:${Version.androidViewModel}"

  const val androidxAppCompat = "androidx.appcompat:appcompat:${Version.androidxAppCompat}"
  const val androidxConstraintLayout =
    "androidx.constraintlayout:constraintlayout:${Version.androidxConstraintLayout}"
  const val androidxRecyclerView =
    "androidx.recyclerview:recyclerview:${Version.androidxRecyclerView}"

  const val koin = "org.koin:koin-android-scope:${Version.koin}"
  const val koinViewModel = "org.koin:koin-android-viewmodel:${Version.koin}"

  const val kotlinCoroutines =
    "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.kotlinCoroutines}"
  const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}"
  const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"

  const val picasso = "com.squareup.picasso:picasso:${Version.picasso}"
  const val picassoDownloader =
    "com.jakewharton.picasso:picasso2-okhttp3-downloader:${Version.picassoDownloader}"
  const val playServicesLocation =
    "com.google.android.gms:play-services-location:${Version.playServicesLocation}"

  const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
  const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
}
