plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":ui:paging-compose"))
            api(project(":ui:common"))
            implementation(project(":core:domain"))
            implementation(project(":core:platform"))
            implementation(project(":core:util"))
            implementation(libs.androidx.paging.common)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.nav3.runtime)
        }
    }
}

android {
    namespace = "me.andannn.aozora.ui.feature"
}
