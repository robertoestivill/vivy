plugins {
  id("com.android.application")
  id("kotlin-android")
}

android {
  compileSdkVersion(29)
  defaultConfig {
    applicationId = "com.robertoestivill.vivy"
    minSdkVersion(26)
    targetSdkVersion(29)
    versionCode = 1
    versionName = "1.0"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
}

dependencies {
  implementation(AppDependency.androidLiveData)
  implementation(AppDependency.androidViewModel)

  implementation(AppDependency.androidxAppCompat)
  implementation(AppDependency.androidxConstraintLayout)
  implementation(AppDependency.androidxRecyclerView)

  implementation(AppDependency.koin)
  implementation(AppDependency.koinViewModel)

  implementation(AppDependency.kotlinStdlib)
  implementation(AppDependency.kotlinCoroutines)
  implementation(AppDependency.okhttpLogging)

  implementation(AppDependency.picasso)
  implementation(AppDependency.picassoDownloader)
  implementation(AppDependency.playServicesLocation)

  implementation(AppDependency.retrofit)
  implementation(AppDependency.retrofitGson)
}
