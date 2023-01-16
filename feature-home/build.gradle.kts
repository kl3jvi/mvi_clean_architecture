plugins {
    id("com.android.library")
    id("klejvi.plugin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
}
android {
    defaultConfig {

        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion
        testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(project(Dependencies.Modules.domain))
    implementation(project(Dependencies.Modules.data))
    implementation(project(Dependencies.Modules.common))

    implementation(Dependencies.Navigation.fragment)
    implementation(Dependencies.Navigation.ui)

    implementation(kotlin("reflect"))
    implementation(Dependencies.Image.glide)
    implementation(project(mapOf("path" to ":core-model")))

    Dependencies.AndroidLifecycle.libList.forEach { implementation(it) }
    kapt(Dependencies.Image.glideKapt)

    Dependencies.AndroidX.libList.forEach { implementation(it) }
    Dependencies.AndroidUI.libList.forEach { implementation(it) }
    Dependencies.AndroidLifecycle.libList.forEach { implementation(it) }

    // Unit Testing
    testImplementation(Dependencies.Testing.coroutine)
    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach { testImplementation(it) }
    // Android Testing
    Dependencies.Testing(Dependencies.Testing.Type.ANDROID)
        .forEach { androidTestImplementation(it) }
}
