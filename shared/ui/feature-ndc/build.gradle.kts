plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kmp.ext")
    alias(libs.plugins.kotlin.compose)
}

kmpExt {
    withAndroid()
    withIOS()
}

kotlin {
    androidLibrary {
        namespace = "me.andannn.aozora.ui.feature.ndc"
    }
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:ui:common"))
        }
    }
}
