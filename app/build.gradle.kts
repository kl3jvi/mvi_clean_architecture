plugins {
    id("com.android.application")
    kotlin("android")
    id("klejvi.plugin.android")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version "1.9.22"
}

androidPlugin {
    buildType = klejvi.plugin.android.BuildType.App
}

android {
    namespace = "com.kl3jvi.takeawaytask"
    
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
    
    kotlinOptions {
        jvmTarget = "17"
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
}
