plugins {
    id("melodify.kmp.library")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.io.core)
            implementation(libs.ktor.client.core)
            implementation(libs.okio)
        }
    }
}

android {
    namespace = "me.andannn.core.util"
}
