plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kmp.ext")
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsCompose)
}

kmpExt {
    withAndroid()
    withIOS()
}

kotlin {
    androidLibrary {
        namespace = "me.andannn.aozora.core.data"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":shared:domain"))
            implementation(project(":shared:datastore"))
            implementation(project(":shared:database"))
            implementation(project(":shared:pagesource"))
            implementation(project(":shared:service"))
            implementation(libs.kotlinx.io.core)
            implementation(libs.androidx.paging.common)
            implementation(libs.room.runtime)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

compose.resources {
    generateResClass = auto
}
