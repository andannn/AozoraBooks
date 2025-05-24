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

        androidMain.dependencies {
            implementation(libs.play.services.ads)
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = auto
}

android {
    namespace = "me.andannn.aozora.ui.common"
}
