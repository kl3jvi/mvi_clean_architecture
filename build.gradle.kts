import org.gradle.api.tasks.testing.logging.TestLogEvent


//Adding the Google and maven repositories to all projects.
allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

}

//Configuring the Kotlin compiler to use the experimental coroutines API and to treat all warnings as errors.
subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            @Suppress("SuspiciousCollectionReassignment")
            freeCompilerArgs += listOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
            allWarningsAsErrors = true
        }
    }

//    Configuring the test logging.
    tasks.withType<Test> {
        testLogging {
            events = setOf(
                TestLogEvent.STANDARD_OUT,
                TestLogEvent.STARTED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED,
            )
            showStandardStreams = true
        }
    }
}



buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Config.AndroidClassPath.gradlePlugin)
        classpath(Config.AndroidClassPath.kotlinPlugin)
        classpath(Config.AndroidClassPath.navigation)
        classpath(Config.AndroidClassPath.daggerHilt)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}