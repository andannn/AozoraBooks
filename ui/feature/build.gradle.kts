plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":ui:common"))
            implementation(project(":core:common"))
            implementation(project(":core:data"))
            implementation(project(":core:pagesource"))
        }
    }
}

android {
    namespace = "me.andannn.aozora.ui.feature"
}
