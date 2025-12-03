plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:domain"))
            api(libs.retainedmodel)
            api(libs.nav3.runtime)
            api(project(":shared:platform"))
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
    namespace = "me.andannn.aozora.core.ui.common"
}
