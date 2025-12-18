import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.konan.target.Family

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
        androidResources.enable = true
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

// Copy Db schemas to apple os Main bundle folder
tasks.withType<KotlinNativeLink>().configureEach {
    val konanTarget = binary.target.konanTarget

    val isAppleTarget =
        konanTarget.family in
            listOf(
                Family.IOS,
                Family.TVOS,
                Family.WATCHOS,
            )

    if (isAppleTarget) {
        val inputSchemaDir = layout.projectDirectory.dir("schemas")
        val outputSchemaDir = destinationDirectory.dir("schemas")
        doLast {
            val srcFile = inputSchemaDir.asFile
            val destFile = outputSchemaDir.get().asFile

            if (srcFile.exists()) {
                srcFile.copyRecursively(target = destFile, overwrite = true)

                println("[CopySchemas] Copied schemas to: ${outputSchemaDir.get().asFile.absolutePath}")
            }
        }
    }
}
dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}
