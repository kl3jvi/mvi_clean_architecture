plugins {
    id("com.android.library")
    id("klejvi.plugin.android")
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
    api(project(Dependencies.Modules.model))
    implementation(project(Dependencies.Modules.persistence))
    implementation(project(Dependencies.Modules.common))


    implementation(Dependencies.Kotlin.serialization)
    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Coroutines.android)


    implementation(Dependencies.Room.ktx)

    kapt(Dependencies.Room.compiler)
    kaptTest(Dependencies.Hilt.hiltAndroidCompiler)


    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach { testImplementation(it) }
    testImplementation(Dependencies.Testing.coroutine)

    Dependencies.Testing(Dependencies.Testing.Type.ANDROID)
        .forEach { androidTestImplementation(it) }
    androidTestImplementation(Dependencies.Testing.coroutine)


}
