plugins {
    id("melodify.kmp.library")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.io.core)
        }
    }
}

android {
    namespace = "me.andannn.aozora.core.common"
}