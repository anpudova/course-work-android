pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FinAppProject"
include(":app")

include(":core")
include(":core:designsystem")
include(":core:utils")
include(":core:db")

include(":feature:profile")
include(":feature:home")
include(":feature:materials")

include(":feature:profile:api")
include(":feature:profile:impl")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:materials:api")
include(":feature:materials:impl")
