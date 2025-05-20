import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("melodify.kmp.application")
    id("melodify.compose.multiplatform.application")
    alias(libs.plugins.google.service)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.serialization)
}

android {
    namespace = "me.andannn.aozora"

    defaultConfig {
        applicationId = "me.andannn.aozora"
        versionCode = (project.findProperty("VERSION_CODE") as? String?)?.toIntOrNull() ?: error("No version code found")
        versionName = project.findProperty("VERSION_NAME") as String? ?: error("No version name found")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            val shouldSign = project.findProperty("android.releaseSigning") == "true"
            if (shouldSign) {
                signingConfigs {
                    create("release") {
                        storeFile = file(System.getenv("SIGNING_KEYSTORE_PATH"))
                        storePassword = System.getenv("KEYSTORE_PASSWORD")
                        keyAlias = System.getenv("KEY_ALIAS")
                        keyPassword = System.getenv("KEY_PASSWORD")
                    }
                }
                signingConfig = signingConfigs.getByName("release")
            } else {
                signingConfig = signingConfigs.getByName("debug")
            }
        }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:data"))
            api(project(":core:platform"))
            implementation(project(":core:service"))
            implementation(project(":core:datastore"))
            implementation(project(":core:database"))
            implementation(project(":core:pagesource"))
            implementation(project(":ui:common"))
            implementation(project(":ui:feature"))
            implementation(libs.napier)
            implementation(libs.circuit.foundation)
        }

        androidMain.dependencies {
            implementation(libs.play.services.ads)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.runtime.ktx)

            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytics)
        }
    }

    targets.withType<KotlinNativeTarget>().all {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true // or false, depending on your use case
            export(project(":core:platform"))
        }
    }
}

dependencies {
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
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
