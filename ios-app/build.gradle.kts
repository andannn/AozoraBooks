import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    sourceSets {
        iosMain.dependencies {
            api(project(":shared:app"))
            api(project(":shared:platform"))
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    )

    targets.withType<KotlinNativeTarget>().all {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true // or false, depending on your use case
            export(project(":shared:platform"))
            export(project(":shared:app"))
        }
    }
}

tasks.register("updateIosVersion") {
    group = "versioning"
    description = "Update iOS Info.plist with versionName and versionCode"

    val plistPath = project.rootDir.resolve("iosApp/iosApp/Info.plist")
    val versionCode = (project.findProperty("VERSION_CODE") as? String?)?.toIntOrNull() ?: error("No version code found")
    val versionName = project.findProperty("VERSION_NAME") as String? ?: error("No version name found")

    doLast {
        val plistFile = plistPath.readText()
        val updated =
            plistFile
                .replace(
                    Regex("<key>CFBundleShortVersionString</key>\\s*<string>.*?</string>"),
                    "<key>CFBundleShortVersionString</key>\n\t<string>$versionName</string>",
                ).replace(
                    Regex("<key>CFBundleVersion</key>\\s*<string>.*?</string>"),
                    "<key>CFBundleVersion</key>\n\t<string>$versionCode</string>",
                )

        plistPath.writeText(updated)
        println("✅ iOS Info.plist updated to version $versionName ($versionCode)")
    }
}
