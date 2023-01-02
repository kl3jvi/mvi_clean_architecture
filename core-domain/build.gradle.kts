plugins {
    id("com.android.library")
    id("klejvi.plugin.android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "1.4.21"
}

android {
//    Junit requires min sdk 14 // added to remove the warning for running androidTest
    defaultConfig {
        minSdk = Config.minSdkVersion
        testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner
    }
}

dependencies {
    // Adding a dependency to the project.
    implementation(project(Dependencies.Modules.common))
    implementation(project(Dependencies.Modules.model))


    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Coroutines.android)
    implementation(Dependencies.Kotlin.serialization)

    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach(::testImplementation)
    testImplementation(Dependencies.Testing.coroutine)

    Dependencies.Testing(Dependencies.Testing.Type.ANDROID).forEach(::androidTestImplementation)
    androidTestImplementation(Dependencies.Testing.coroutine)

}
