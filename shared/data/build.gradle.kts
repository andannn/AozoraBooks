plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:domain"))
            implementation(project(":shared:datastore"))
            implementation(project(":shared:database"))
            implementation(project(":shared:pagesource"))
            implementation(project(":shared:service"))
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
