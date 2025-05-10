plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(project(":core:data"))
            implementation(project(":core:parser"))
            implementation(project(":core:pagesource"))
            implementation(project(":core:datastore"))
            implementation(project(":core:common"))
            implementation(project(":core:service"))
            implementation(project(":core:util"))
            implementation(libs.ksoup)
            implementation(libs.room.runtime)
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
            implementation(libs.kotlinx.io.core)
            implementation(libs.kotlinx.io.core)
            implementation(libs.androidx.paging.common)
        }
    }
}

android {
    namespace = "me.andannn.sharedtest"
}


