package com.andanana.melodify.util

import org.gradle.api.Project
import org.jlleitschuh.gradle.ktlint.KtlintExtension

fun Project.configureKtLint(extension: KtlintExtension) {
    with(extension) {
        verbose.set(true)
        debug.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")
        additionalEditorconfig.set(
            mapOf(
                "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
            ),
        )
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }
}
