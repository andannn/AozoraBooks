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
        maven("https://central.sonatype.com/repository/maven-snapshots/")
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
include(":android-app")
include(":ios-app")
include(":shared:app")
include(":shared:domain")
include(":shared:datastore")
include(":shared:pagesource")
include(":shared:service")
include(":shared:util")
include(":shared:ui:feature")
include(":shared:ui:common")
include(":shared:data")
include(":shared:ui:paging-compose")
include(":shared:database")
include(":shared:platform")
include(":shared:syncer")
