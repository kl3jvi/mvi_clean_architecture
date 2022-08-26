rootProject.name = "Takeaway Task"
//Telling gradle to use the build.gradle.kts file instead of the default build.gradle file.
rootProject.buildFileName = "build.gradle.kts"

include(
    ":app",
    ":core-data",
    ":core-persistence",
    ":core-domain",
    ":core-common",
    ":feature-home",
    ":benchmark"
)
include(":core-model")
