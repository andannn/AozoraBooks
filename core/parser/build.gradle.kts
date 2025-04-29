plugins {
    id("melodify.kmp.library")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:util"))
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.io.core)
            implementation(libs.ksoup)
        }
    }
}

android {
    namespace = "me.andannn.aozora.core.parser"
}