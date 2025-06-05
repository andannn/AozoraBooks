plugins {
    id("melodify.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            implementation(project(":core:datastore"))
            implementation(project(":core:database"))
            implementation(project(":core:pagesource"))
            implementation(project(":core:service"))
            implementation(libs.androidx.paging.common)
            implementation(libs.room.runtime)
        }
    }
}

android {
    namespace = "me.andannn.aozora.core.data"
}
