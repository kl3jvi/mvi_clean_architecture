plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()

}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.android.tools.build:gradle:7.2.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    implementation(gradleApi())
}
