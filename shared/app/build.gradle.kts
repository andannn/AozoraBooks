plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.parcelize)
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
            api(project(":shared:platform"))
            implementation(project(":shared:ui:feature"))
            implementation(libs.napier)
            implementation(libs.circuit.foundation)
            implementation(libs.nav3.ui)
        }
    }
}
