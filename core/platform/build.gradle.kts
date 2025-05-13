
android {
    namespace = "me.andannn.platform"
}

plugins {
    id("melodify.kmp.library")
}

kotlin {
    sourceSets.apply {
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
        }
    }
}
