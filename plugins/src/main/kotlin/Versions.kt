/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2022-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

import org.gradle.api.JavaVersion
import org.gradle.jvm.toolchain.JavaLanguageVersion

object Versions {
    const val VERSION_CODE = 2
    val VERSION_NAME = "1.0.1"

    /**
     * Compile SDK version. Must be updated when a new Android version is released.
     * When updating COMPILE_SDK, please also update BUILD_TOOLS_VERSION.
     */
    const val COMPILE_SDK = 37

    /**
     * Build tools version. Must be kept in sync with COMPILE_SDK.
     * The value is used by the release script.
     */
    @Suppress("unused")
    private const val BUILD_TOOLS_VERSION = "37.0.0"

    /**
     * Target SDK version. Should be kept up to date with COMPILE_SDK.
     */
    const val TARGET_SDK = 37

    /**
     * Minimum SDK version for FOSS builds.
     */
    private const val MIN_SDK_FOSS = 24

    /**
     * Minimum SDK version for Enterprise builds.
     */
    private const val MIN_SDK_ENTERPRISE = 33

    /**
     * minSdkVersion that will be set in the Android Manifest.
     */
    val minSdk = if (isEnterpriseBuild) MIN_SDK_ENTERPRISE else MIN_SDK_FOSS

    /**
     * Java version used for compilation.
     * Update this value when you want to use a newer Java version.
     */
    private const val JAVA_VERSION = 21

    val javaVersion: JavaVersion = JavaVersion.toVersion(JAVA_VERSION)
    val javaLanguageVersion: JavaLanguageVersion = JavaLanguageVersion.of(JAVA_VERSION)

    init {
        require(BUILD_TOOLS_VERSION.startsWith(COMPILE_SDK.toString())) { "When updating COMPILE_SDK, please also update BUILD_TOOLS_VERSION" }
    }
}
