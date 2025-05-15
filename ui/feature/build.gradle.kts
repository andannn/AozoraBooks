plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":ui:paging-compose"))
            implementation(project(":ui:common"))
            implementation(project(":core:common"))
            implementation(project(":core:platform"))
            implementation(project(":core:util"))
            implementation(project(":core:data"))
            implementation(project(":core:pagesource"))
            implementation(libs.androidx.paging.common)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "me.andannn.aozora.ui.feature"
}
