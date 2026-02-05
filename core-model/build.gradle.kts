plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("klejvi.plugin.android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "1.9.22"
}

android {
    namespace = "com.kl3jvi.model"
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdkVersion

        testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(Dependencies.Kotlin.serialization)
}
