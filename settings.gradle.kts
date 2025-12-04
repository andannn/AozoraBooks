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
include(":shared:syncer")

include(":shared:domain")
include(":shared:data")

include(":shared:datastore")
include(":shared:pagesource")
include(":shared:service")
include(":shared:util")
include(":shared:database")
include(":shared:platform")

include(":shared:ui:common")
include(":shared:ui:feature-author")
include(":shared:ui:feature-about")
include(":shared:ui:feature-ndc")
include(":shared:ui:feature-license")
include(":shared:ui:feature-authorpages")
include(":shared:ui:feature-bookcard")
include(":shared:ui:feature-index-pages")
include(":shared:ui:feature-reader")
include(":shared:ui:feature-library")
include(":shared:ui:feature-search")
include(":shared:ui:feature-search-input")
include(":shared:ui:feature-search-result")
