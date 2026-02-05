plugins {
    id("com.android.library")
    id("klejvi.plugin.android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "1.9.22"
}

android {
    namespace = "com.kl3jvi.domain"
    
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

    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Coroutines.android)
    implementation(Dependencies.Kotlin.serialization)

    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach(::testImplementation)
    testImplementation(Dependencies.Testing.coroutine)

    Dependencies.Testing(Dependencies.Testing.Type.ANDROID).forEach(::androidTestImplementation)
    androidTestImplementation(Dependencies.Testing.coroutine)
}
