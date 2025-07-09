plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.io.core)
            implementation(libs.ktor.client.core)
            implementation(libs.okio)
            implementation(libs.ksoup)
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "me.andannn.core.util"
}
