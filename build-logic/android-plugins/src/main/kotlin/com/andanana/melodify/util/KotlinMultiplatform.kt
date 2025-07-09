package com.andanana.melodify.util

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

@ExperimentalKotlinGradlePluginApi
fun Project.configureKotlinMultiplatform(extension: KotlinMultiplatformExtension) {
    with(extension) {
        compilerOptions {
            androidTarget {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)

                // this is experimental API and will likely change in the future into more robust DSL
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                instrumentedTestVariant {
                    // !!! this makes instrumented tests depends on commonTest source set.
                    sourceSetTree.set(KotlinSourceSetTree.test)
                }
            }
        }

        listOf(
            iosArm64(),
            iosSimulatorArm64(),
        )

        sourceSets.apply {
            commonMain.dependencies {
                val bom = libs.findLibrary("koin-bom").get()
                implementation(project.dependencies.platform(bom))
                implementation(libs.findLibrary("koin.core").get())

                implementation(libs.findLibrary("kotlinx.collections.immutable").get())
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                implementation(libs.findLibrary("napier").get())
                implementation(libs.findLibrary("kotlinx.datetime").get())
            }

            androidMain.dependencies {
                implementation(libs.findLibrary("koin.android").get())
            }

            commonTest.dependencies {
                implementation(libs.findLibrary("kotlin.test").get())
                implementation(libs.findLibrary("kotlinx.coroutines.test").get())
            }

            androidInstrumentedTest.dependencies {
                implementation(libs.findLibrary("androidx.test.runner").get())
                implementation(libs.findLibrary("androidx.test.core.ktx").get())
                implementation(libs.findLibrary("androidx.test.monitor").get())
            }
        }
    }
}
