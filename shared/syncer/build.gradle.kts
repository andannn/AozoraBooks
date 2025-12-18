plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kmp.ext")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsCompose)
}

kmpExt {
    withAndroid()
    withIOS()
}

kotlin {
    androidLibrary {
        namespace = "me.andannn.aozora.syncer"
        androidResources.enable = true
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:domain"))
            implementation(project(":shared:database"))
            implementation(project(":shared:datastore"))
            implementation(project(":shared:util"))
            implementation(project(":shared:platform"))
            implementation(libs.kotlinx.io.core)
            implementation(libs.ktor.client.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.work.runtime.ktx)
        }
    }
}

compose.resources {
    generateResClass = auto
}
