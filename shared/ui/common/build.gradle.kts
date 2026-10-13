plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kmp.ext")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)
    alias(libs.plugins.jetbrainsCompose)
}

kmpExt {
    withAndroid()
    withIOS()
}

kotlin {
    androidLibrary {
        namespace = "me.andannn.aozora.core.ui.common"
        androidResources.enable = true
    }
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:domain"))
            api(libs.retainedmodel)
            api(project(":shared:platform"))
            api(project(":shared:util"))
            api(libs.androidx.paging.compose)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = auto
}
