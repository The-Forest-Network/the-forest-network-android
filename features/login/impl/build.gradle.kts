import config.BuildTimeConfig
import extension.buildConfigFieldStr
import extension.setupDependencyInjection
import extension.testCommonDependencies

/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2022-2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

plugins {
    id("io.element.android-compose-library")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.element.android.features.login.impl"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigFieldStr(
            name = "URL_REQUEST_ACCOUNT",
            value = BuildTimeConfig.URL_REQUEST_ACCOUNT ?: "https://element.io",
        )
    }

    lint {
        // Pre-existing StringFormatCount errors from Forest Network's branded onboarding
        // strings losing their %1$s placeholder while untranslated locales kept it. See
        // https://github.com/The-Forest-Network/the-forest-network-android/issues/18
        baseline = file("lint-baseline.xml")
    }

    buildTypes {
        val elementClassicPackageKey = "elementClassicPackage"
        val elementClassicPackage = "im.vector.app"
        val elementClassicPackageDebug = "$elementClassicPackage.debug"
        val elementClassicPackageNightly = "$elementClassicPackage.nightly"
        getByName("release") {
            manifestPlaceholders[elementClassicPackageKey] = elementClassicPackage
            buildConfigFieldStr(elementClassicPackageKey, elementClassicPackage)
        }
        getByName("debug") {
            manifestPlaceholders[elementClassicPackageKey] = elementClassicPackageDebug
            buildConfigFieldStr(elementClassicPackageKey, elementClassicPackageDebug)
        }
        register("nightly") {
            matchingFallbacks += listOf("release")
            manifestPlaceholders[elementClassicPackageKey] = elementClassicPackageNightly
            buildConfigFieldStr(elementClassicPackageKey, elementClassicPackageNightly)
        }
    }
}

setupDependencyInjection()

dependencies {
    implementation(projects.appconfig)
    implementation(projects.features.enterprise.api)
    implementation(projects.features.preferences.api)
    implementation(projects.features.rageshake.api)
    implementation(projects.libraries.core)
    implementation(projects.libraries.androidutils)
    implementation(projects.libraries.architecture)
    implementation(projects.libraries.matrix.api)
    implementation(projects.libraries.matrix.api)
    implementation(projects.libraries.designsystem)
    implementation(projects.libraries.testtags)
    implementation(projects.libraries.uiStrings)
    implementation(projects.libraries.permissions.api)
    implementation(projects.libraries.sessionStorage.api)
    implementation(projects.libraries.qrcode)
    implementation(projects.libraries.oauth.api)
    implementation(projects.libraries.preferences.api)
    implementation(projects.libraries.uiUtils)
    implementation(projects.libraries.wellknown.api)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.webkit)
    implementation(libs.serialization.json)
    api(projects.features.login.api)

    testCommonDependencies(libs, true)
    testImplementation(projects.features.login.test)
    testImplementation(projects.features.enterprise.test)
    testImplementation(projects.features.preferences.test)
    testImplementation(projects.libraries.preferences.test)
    testImplementation(projects.libraries.matrix.test)
    testImplementation(projects.libraries.oauth.test)
    testImplementation(projects.libraries.permissions.test)
    testImplementation(projects.libraries.sessionStorage.test)
    testImplementation(projects.libraries.wellknown.test)
    testImplementation(libs.androidx.camera.camera2)
    testImplementation(libs.androidx.camera.lifecycle)
}
