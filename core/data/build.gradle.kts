plugins {
    id("melodify.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:datastore"))
            implementation(project(":core:database"))
            implementation(project(":core:service"))
            implementation(libs.androidx.paging.common)
        }
    }
}

android {
    namespace = "me.andannn.aozora.core.data"
}
