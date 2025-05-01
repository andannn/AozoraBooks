// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.parcelize) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.ktlint) apply false
}
