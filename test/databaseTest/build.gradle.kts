plugins {
    id("melodify.kmp.library")
}

android {
    namespace = "me.andannn.databasetest"
}

kotlin {
    jvm()
    sourceSets {
        jvmTest.dependencies {
            implementation(project(":core:database"))
            implementation(libs.androidx.room.testing)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.room.runtime)
            implementation(libs.okio)
        }
    }
}
