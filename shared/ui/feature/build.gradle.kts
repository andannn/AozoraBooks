plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:ui:paging-compose"))
            api(project(":shared:ui:common"))
            implementation(project(":shared:domain"))
            implementation(project(":shared:platform"))
            implementation(project(":shared:util"))
            implementation(libs.androidx.paging.common)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.nav3.runtime)
        }
    }
}

android {
    namespace = "me.andannn.aozora.core.ui.feature"
}
