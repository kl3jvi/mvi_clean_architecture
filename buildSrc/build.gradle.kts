plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

// Configure Java toolchain for compatibility with JDK 17+
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.android.tools.build:gradle:8.2.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    // JavaPoet for Hilt compatibility with JDK 17+
    implementation("com.squareup:javapoet:1.13.0")
    implementation(gradleApi())
}
