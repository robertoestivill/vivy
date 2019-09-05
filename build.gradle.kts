buildscript {
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath(BuildDependency.androidPlugin)
    classpath(BuildDependency.kotlinPlugin)
  }
}

allprojects {
  repositories {
    google()
    jcenter()
  }
}
