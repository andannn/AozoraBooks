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
        namespace = "me.andannn.core.util"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.io.core)
            implementation(libs.ktor.client.core)
            implementation(libs.okio)
            implementation(libs.ksoup)
            implementation(libs.kotlin.test)
        }
    }
}
