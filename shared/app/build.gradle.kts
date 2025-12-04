plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)
}

android {
    namespace = "me.andannn.aozora.app"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:domain"))
            implementation(project(":shared:data"))
            implementation(project(":shared:syncer"))
            implementation(project(":shared:platform"))
            implementation(project(":shared:ui:feature-author"))
            implementation(project(":shared:ui:feature-about"))
            implementation(project(":shared:ui:feature-ndc"))
            implementation(project(":shared:ui:feature-license"))
            implementation(project(":shared:ui:feature-authorpages"))
            implementation(project(":shared:ui:feature-bookcard"))
            implementation(project(":shared:ui:feature-index-pages"))
            implementation(project(":shared:ui:feature-reader"))
            implementation(project(":shared:ui:feature-library"))
            implementation(project(":shared:ui:feature-search"))
            implementation(project(":shared:ui:feature-search-input"))
            implementation(project(":shared:ui:feature-search-result"))
            implementation(libs.napier)
            implementation(libs.nav3.ui)
        }
    }
}
