plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:ui:common"))
            implementation(libs.navigationevent.compose)
        }
    }
}

android {
    namespace = "me.andannn.aozora.ui.feature.reader"
}
