object Config {

    const val applicationId = "com.kl3jvi.takeaway_task"
    const val compileSdk = 32
    const val minSdkVersion = 23
    const val targetSdkVersion = 32
    const val buildTools = "30.0.3"
    const val versionCode = 1
    const val versionName = "1.0.0"

    object AndroidClassPath {

        const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
        const val kotlinPlugin =
            "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
        const val navigation =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"

        const val daggerHilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.DI.hiltVer}"
    }

    object AndroidTestRunner {
        const val instrumentationTestRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}