object Dependencies {

    interface Group {
        val libList: List<String>
    }

    object Modules {
        const val data = ":core-data"
        const val persistence = ":core-persistence"
        const val domain = ":core-domain"
        const val common = ":core-common"
        const val model = ":core-model"
        const val feature_home = ":feature-home"
    }


    object Kotlin : Group {
        private const val core = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}"
        private const val coreJDK7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"
        
        override val libList: List<String>
            get() = listOf(core, coreJDK7)
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }


    object Hilt {
        const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.DI.hiltVer}"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.DI.hiltVer}"
    }

    object AndroidUI : Group {
        private const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidUI.appcompat}"
        private const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.AndroidUI.constraintLayout}"
        private const val material = "com.google.android.material:material:${Versions.AndroidUI.material}"
        private const val fragment = "androidx.fragment:fragment-ktx:${Versions.AndroidKTX.fragment}"
        private const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.AndroidUI.recyclerView}"
        private const val cardView = "androidx.cardview:cardview:${Versions.AndroidUI.cardView}"

        override val libList: List<String>
            get() = listOf(appCompat, constraintLayout, material, fragment, recyclerView, cardView)
    }

    object Navigation {
        const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val testing = "androidx.navigation:navigation-testing:${Versions.navigation}"
    }

    object Image {
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val glideKapt = "com.github.bumptech.glide:compiler:${Versions.glide}"

    }

    object AndroidX : Group {
        private const val core = "androidx.core:core-ktx:${Versions.AndroidKTX.core}"
        private const val annotation = "androidx.annotation:annotation:${Versions.AndroidUI.annotation}"
        private const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidKTX.lifecycle}"
        override val libList: List<String>
            get() = listOf(core, annotation, lifecycle)
    }

    object Room {
        const val ktx = "androidx.room:room-ktx:${Versions.AndroidJetPack.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.AndroidJetPack.room}"
    }


    object AndroidLifecycle : Group {
        private const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidJetPack.lifecycle}"

        override val libList: List<String>
            get() = listOf(viewModel)
    }


    object Testing {

        // Unit Testing
        private const val jUnit = "junit:junit:${Versions.Testing.junit}"
        private const val archCore = "androidx.arch.core:core-testing:${Versions.AndroidJetPack.arch_core}"
        private const val kotlinJUnit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlinVersion}"

        private const val truth = "com.google.truth:truth:${Versions.Testing.truth}"
        private const val turbine = "app.cash.turbine:turbine:${Versions.Testing.turbine}"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"

        private const val hiltTest = "com.google.dagger:hilt-android-testing:${Versions.DI.hiltVer}"
        private const val roomRuntime = "androidx.room:room-runtime:${Versions.AndroidJetPack.room}"
        private const val roomTesting = "androidx.room:room-testing:${Versions.AndroidJetPack.room}"

        // Android Testing
        private const val androidTestCore = "androidx.test:core:${Versions.Testing.androidTestCore}"

        // Non Private for single Import
        const val androidJUnit = "androidx.test.ext:junit-ktx:${Versions.Testing.androidTestJUnit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.Testing.espresso}"
        const val uiAutomator = "androidx.test.uiautomator:uiautomator:${Versions.Testing.uiAutomator}"
        const val macroBenchmarkJunit = "androidx.benchmark:benchmark-macro-junit4:${Versions.Testing.macroBenchmarkJunit}"



        private const val testRunner = "androidx.test:runner:${Versions.Testing.androidTestCore}"
        private const val testRules = "androidx.test:rules:${Versions.Testing.androidTestCore}"

        operator fun invoke(type: Type) = when (type) {
            Type.UNIT -> listOf(
                jUnit, archCore, kotlinJUnit, truth, turbine, hiltTest
            )

            Type.ANDROID -> listOf(
                androidTestCore, archCore, turbine, androidJUnit, roomRuntime, roomTesting, testRunner, testRules, truth
            )
        }

        enum class Type {
            UNIT, ANDROID
        }
    }
}


