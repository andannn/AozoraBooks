plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kmp.ext")
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.compose)
}

kmpExt {
    withAndroid()
    withIOS()
}

kotlin {
    androidLibrary {
        namespace = "me.andannn.aozora.core.common"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.paging.common)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.io.core)
        }
    }
}
