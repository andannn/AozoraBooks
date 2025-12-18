plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kmp.ext")
    alias(libs.plugins.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kmpExt {
    withAndroid {
        enableHostTest = false
        enableDeviceTest = true
        includeDeviceTestToCommonTest = true
    }
    withIOS()
}

kotlin {
    androidLibrary {
        namespace = "me.andannn.aozora.core.database"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.room.runtime)
            implementation(libs.room.paging)
            implementation(libs.okio)
        }

        commonTest.dependencies {
            implementation(libs.androidx.room.testing)
            implementation(libs.okio)
        }

        iosMain.dependencies {
            implementation(libs.androidx.sqlite.bundled)
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
}
