plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            implementation(project(":core:datastore"))
            implementation(project(":core:database"))
            implementation(project(":core:pagesource"))
            implementation(project(":core:service"))
            implementation(libs.kotlinx.io.core)
            implementation(libs.androidx.paging.common)
            implementation(libs.room.runtime)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "me.andannn.aozora.core.data"
}
