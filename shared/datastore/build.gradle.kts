plugins {
    id("melodify.kmp.library")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:domain"))

            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
            implementation(libs.kotlinx.io.core)
        }
    }
}

android {
    namespace = "me.andannn.aozora.core.datastore"
}
