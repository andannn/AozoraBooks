plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:ui:common"))
        }
    }
}

android {
    namespace = "me.andannn.aozora.ui.feature.ndc"
}
