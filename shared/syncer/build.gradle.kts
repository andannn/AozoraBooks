plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

android {
    namespace = "me.andannn.aozora.syncer"
}

kotlin {
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
