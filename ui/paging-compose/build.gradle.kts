plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.paging.common)
        }
    }
}

android {
    namespace = "androidx.paging.compose"
}
