plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            api(project(":core:platform"))
        }
    }
}

android {
    namespace = "me.andannn.aozora.ui.common"
}
