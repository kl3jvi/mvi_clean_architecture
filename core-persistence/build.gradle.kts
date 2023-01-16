plugins {
    id("com.android.library")
    id("klejvi.plugin.android")
}

android {
    defaultConfig {
        minSdk = Config.minSdkVersion
        testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner
    }
}

dependencies {
    implementation(project(Dependencies.Modules.common))

    implementation(Dependencies.Room.ktx)
    implementation(project(mapOf("path" to ":core-model")))
    kapt(Dependencies.Room.compiler)

    implementation(Dependencies.Coroutines.android)

    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach { testImplementation(it) }

    androidTestImplementation(Dependencies.Testing.coroutine)
    Dependencies.Testing(Dependencies.Testing.Type.ANDROID)
        .forEach { androidTestImplementation(it) }
}
