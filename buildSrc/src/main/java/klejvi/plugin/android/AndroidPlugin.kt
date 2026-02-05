package klejvi.plugin.android

import Config
import Dependencies
import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies

open class AndroidPlugin : Plugin<Project> {

    /**
     * It creates an extension for the project and configures the plugins, android and dependencies.
     *
     * @param project Project - The project that the plugin is being applied to.
     */
    override fun apply(project: Project) {
        val extension = project.extensions.create<AndroidPluginExtension>("androidPlugin")
        project.configurePlugins(extension.buildType)
        project.configureAndroid()
        project.configureDependencies()
    }

    private fun androidPlugins() = listOf("kotlin-android")

    /**
     * It applies the Android plugin to Android apps and libraries, and the Kotlin plugin to all other projects
     *
     * @param buildType The type of build we're configuring.
     */
    private fun Project.configurePlugins(buildType: BuildType) = listOf(
        when (buildType) {
            BuildType.AndroidLibrary,
            BuildType.App -> androidPlugins()

            BuildType.Library -> listOf("kotlin")
        },
        listOf("kotlin-kapt")
    ).flatten().also { println("AndroidPlugin: applying plugins $it") }.forEach(plugins::apply)

    /**
     * It configures the Android plugin for the project.
     */
    private fun Project.configureAndroid() = extensions.getByType(BaseExtension::class.java).run {
        compileSdkVersion(Config.compileSdk)

        defaultConfig {
            multiDexEnabled = true
            versionCode = Config.versionCode
            versionName = Config.versionName
            testInstrumentationRunner = Config.AndroidTestRunner.instrumentationTestRunner

            /* Excluding some files from the APK. */
            @Suppress("DEPRECATION")
            packagingOptions {
                resources.excludes.addAll(
                    listOf(
                        "META-INF/DEPENDENCIES",
                        "META-INF/LICENSE",
                        "META-INF/LICENSE.txt",
                        "META-INF/license.txt",
                        "META-INF/NOTICE",
                        "META-INF/NOTICE.txt",
                        "META-INF/notice.txt",
                        "META-INF/ASL2.0",
                        "META-INF/*.kotlin_module",
                        "META-INF/AL2.0",
                        "META-INF/LGPL2.1",
                        "META-INF/gradle/incremental.annotation.processors"
                    )
                )
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            buildTypes {
                getByName("debug") {
                    isDebuggable = true
                }

                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        file("proguard-rules.pro")
                    )
                }
            }
        }

        testOptions {
            unitTests.isReturnDefaultValues = true
            animationsDisabled = true
        }
    }

    private fun Project.configureDependencies() = dependencies {
        fun kapt(definition: Any) = "kapt"(definition)
        fun implementation(definition: Any) = "implementation"(definition)
        fun testImplementation(definition: Any) = "testImplementation"(definition)
        fun androidTestImplementation(definition: Any) = "androidTestImplementation"(definition)

        Dependencies.Kotlin.libList.forEach { implementation(it) }
        with(Dependencies.Hilt) {
            implementation(hiltAndroid)
            kapt(hiltAndroidCompiler)
        }
    }
}

open class AndroidPluginExtension {
    var buildType = BuildType.AndroidLibrary
}

enum class BuildType {
    Library,
    AndroidLibrary,
    App
}
