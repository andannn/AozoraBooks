plugins {
    id("melodify.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
        }
    }
}

android {
    namespace = "me.andannn.aozora.core.data"
}
