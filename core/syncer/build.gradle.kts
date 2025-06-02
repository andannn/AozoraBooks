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
            implementation(project(":core:database"))
            implementation(project(":core:datastore"))
            implementation(project(":core:util"))
            implementation(libs.kotlinx.io.core)
            implementation(libs.ktor.client.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.work.runtime.ktx)
        }
    }
}
