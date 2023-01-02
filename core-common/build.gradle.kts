plugins {
    id("com.android.library")
    id("klejvi.plugin.android")
}

android {
//    Junit requires min sdk 14 // added to remove the warning for running androidTest
    defaultConfig {
        minSdk = Config.minSdkVersion
        testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner
    }
}


dependencies {
    implementation(Dependencies.Coroutines.android)

    // Unit Testing
    testImplementation(Dependencies.Testing.coroutine)
    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach(::testImplementation)

    // Android Testing
    Dependencies.Testing(Dependencies.Testing.Type.ANDROID)
        .forEach(::androidTestImplementation)
}