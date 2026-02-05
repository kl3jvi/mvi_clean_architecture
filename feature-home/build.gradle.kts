plugins {
    id("com.android.library")
    id("klejvi.plugin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.kl3jvi.feature_home"
    
    defaultConfig {
        minSdk = Config.minSdkVersion
        testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner
    }
    
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(project(Dependencies.Modules.domain))
    implementation(project(Dependencies.Modules.data))
    implementation(project(Dependencies.Modules.common))
    implementation(project(Dependencies.Modules.model))

    implementation(Dependencies.Navigation.fragment)
    implementation(Dependencies.Navigation.ui)

    implementation(kotlin("reflect"))
    implementation(Dependencies.Image.glide)

    Dependencies.AndroidLifecycle.libList.forEach { implementation(it) }
    kapt(Dependencies.Image.glideKapt)

    Dependencies.AndroidX.libList.forEach { implementation(it) }
    Dependencies.AndroidUI.libList.forEach { implementation(it) }

    // Unit Testing
    testImplementation(Dependencies.Testing.coroutine)
    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach { testImplementation(it) }
    // Android Testing
    Dependencies.Testing(Dependencies.Testing.Type.ANDROID)
        .forEach { androidTestImplementation(it) }
}
