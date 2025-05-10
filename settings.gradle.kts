pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Aosora"
include(":composeApp")
include(":core:common")
include(":core:datastore")
include(":core:parser")
include(":core:pagesource")
include(":core:service")
include(":core:util")
include(":ui:feature")
include(":ui:common")
include(":core:data")
include(":ui:paging-compose")
