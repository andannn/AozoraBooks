plugins {
    id("melodify.kmp.library")
    id("melodify.compose.multiplatform.library")
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

android {
    namespace = "me.andannn.aozora.core.database"
}

kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.room.runtime)
            implementation(libs.okio)
        }

        iosMain.dependencies {
            implementation(libs.androidx.sqlite.bundled)
        }

        jvmTest.dependencies {
            implementation(project(":core:database"))
            implementation(libs.androidx.room.testing)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.room.runtime)
            implementation(libs.okio)
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspJvm", libs.room.compiler)
}
