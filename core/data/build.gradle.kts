plugins {
    id("melodify.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:datastore"))
        }
    }
}

android {
    namespace = "me.andannn.aozora.core.data"
}
