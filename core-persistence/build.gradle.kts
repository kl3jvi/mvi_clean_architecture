plugins {
    id("com.android.library")
    id("klejvi.plugin.android")
}

android {
    namespace = "com.kl3jvi.persistence"
    
    defaultConfig {
        minSdk = Config.minSdkVersion
        testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(Dependencies.Modules.common))
    implementation(project(Dependencies.Modules.model))

    implementation(Dependencies.Room.ktx)
    kapt(Dependencies.Room.compiler)

    implementation(Dependencies.Coroutines.android)

    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach { testImplementation(it) }

    androidTestImplementation(Dependencies.Testing.coroutine)
    Dependencies.Testing(Dependencies.Testing.Type.ANDROID)
        .forEach { androidTestImplementation(it) }
}
