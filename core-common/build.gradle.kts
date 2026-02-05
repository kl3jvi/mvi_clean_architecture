plugins {
    id("com.android.library")
    id("klejvi.plugin.android")
}

android {
    namespace = "com.kl3jvi.common"
    
    defaultConfig {
        minSdk = Config.minSdkVersion
        testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(Dependencies.Coroutines.android)
    
    // ViewModel dependencies for MviViewModel base class
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidJetPack.lifecycle}")

    // Unit Testing
    testImplementation(Dependencies.Testing.coroutine)
    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach(::testImplementation)

    // Android Testing
    Dependencies.Testing(Dependencies.Testing.Type.ANDROID)
        .forEach(::androidTestImplementation)
}
