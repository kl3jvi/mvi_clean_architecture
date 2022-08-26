plugins {
    id("com.android.application")
    kotlin("android")
    id("klejvi.plugin.android")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version "1.4.21"
}

androidPlugin {
    buildType = klejvi.plugin.android.BuildType.App
}

android {
    signingConfigs {
        create("release") {
        }
    }
    defaultConfig {
        applicationId = "com.kl3jvi.takeaway_task"
        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion
        testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    buildTypes {
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
}

dependencies {
    implementation(project(Dependencies.Modules.data))
    implementation(project(Dependencies.Modules.persistence))
    implementation(project(Dependencies.Modules.domain))
    implementation(project(Dependencies.Modules.common))
    implementation(project(Dependencies.Modules.feature_home))

    Dependencies.AndroidX.libList.forEach { implementation(it) }
    Dependencies.AndroidUI.libList.forEach { implementation(it) }
    Dependencies.AndroidLifecycle.libList.forEach { implementation(it) }

    // Unit Testing
    Dependencies.Testing(Dependencies.Testing.Type.UNIT).forEach { testImplementation(it) }
    // Android Testing
    Dependencies.Testing(Dependencies.Testing.Type.ANDROID)
        .forEach { androidTestImplementation(it) }

    implementation("com.github.kl3jvi.mappy:mappy-core:0.0.1")
    kapt("com.github.kl3jvi.mappy:mappy-processor:0.0.1")

}
