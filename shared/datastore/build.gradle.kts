plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kmp.ext")
    alias(libs.plugins.serialization)
}

kmpExt {
    withAndroid()
    withIOS()
}

kotlin {
    androidLibrary {
        namespace = "me.andannn.aozora.core.datastore"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:domain"))

            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
            implementation(libs.kotlinx.io.core)
        }
    }
}
