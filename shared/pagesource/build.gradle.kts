plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kmp.ext")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)
}

kmpExt {
    withAndroid()
    withIOS()
}

kotlin {
    androidLibrary {
        namespace = "me.andannn.aozora.core.pagesource"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.io.core)
            implementation(project(":shared:domain"))
            implementation(project(":shared:util"))
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ksoup)
            implementation(libs.ktor.client.core)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
    }
}
