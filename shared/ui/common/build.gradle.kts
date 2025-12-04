plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:domain"))
            api(libs.retainedmodel)
            api(libs.nav3.runtime)
            api(project(":shared:platform"))
            api(project(":shared:util"))
            api(libs.androidx.paging.compose)
            implementation(libs.kotlinx.serialization.json)
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
