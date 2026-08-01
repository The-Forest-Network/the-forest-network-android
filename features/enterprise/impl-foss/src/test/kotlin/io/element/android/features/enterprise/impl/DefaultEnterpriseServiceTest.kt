/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2024, 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.enterprise.impl

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.element.android.compound.colors.SemanticColorsLightDark
import io.element.android.compound.tokens.generated.compoundColorsDark
import io.element.android.compound.tokens.generated.compoundColorsLight
import io.element.android.features.enterprise.api.BugReportUrl
import io.element.android.libraries.matrix.test.A_HOMESERVER_URL
import io.element.android.libraries.matrix.test.A_SESSION_ID
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DefaultEnterpriseServiceTest {
    private val brandRest = Color(0xFF535946)
    private val brandHovered = Color(0xFF3D4234)
    private val brandPressed = Color(0xFF2A2D24)
    private val brandSubtle = Color(0xFF6B7060)
    private val brandSelected = Color(0x33535946)
    private val expectedSemanticColors = SemanticColorsLightDark(
        light = compoundColorsLight.copy(
            bgAccentRest = brandRest,
            bgAccentHovered = brandHovered,
            bgAccentPressed = brandPressed,
            bgAccentSelected = brandSelected,
            borderAccentPrimary = brandRest,
            borderAccentSubtle = brandSubtle,
            iconAccentPrimary = brandRest,
            iconAccentTertiary = brandSubtle,
            bgActionPrimaryRest = brandRest,
            bgActionPrimaryHovered = brandHovered,
            bgActionPrimaryPressed = brandPressed,
            borderInteractiveHovered = brandRest,
        ),
        dark = compoundColorsDark.copy(
            bgAccentRest = brandRest,
            bgAccentHovered = brandHovered,
            bgAccentPressed = brandPressed,
            bgAccentSelected = brandSelected,
            borderAccentPrimary = brandRest,
            borderAccentSubtle = brandSubtle,
            iconAccentPrimary = brandRest,
            iconAccentTertiary = brandSubtle,
            bgActionPrimaryRest = brandRest,
            bgActionPrimaryHovered = brandHovered,
            bgActionPrimaryPressed = brandPressed,
            borderInteractiveHovered = brandRest,
        ),
    )

    @Test
    fun `isEnterpriseBuild is false`() {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.isEnterpriseBuild).isFalse()
    }

    @Test
    fun `defaultHomeserverList returns the Forest Network homeserver`() {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.defaultHomeserverList()).containsExactly("https://matrix.theforestnetwork.earth")
    }

    @Test
    fun `isAllowedToConnectToHomeserver is true only for the Forest Network homeserver`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.isAllowedToConnectToHomeserver("https://matrix.theforestnetwork.earth")).isTrue()
        assertThat(defaultEnterpriseService.isAllowedToConnectToHomeserver(A_HOMESERVER_URL)).isFalse()
    }

    @Test
    fun `isEnterpriseUser always return false`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.isEnterpriseUser(A_SESSION_ID)).isFalse()
    }

    @Test
    fun `semanticColorsFlow always emits the Forest Network brand colours`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        defaultEnterpriseService.semanticColorsFlow(null).test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(expectedSemanticColors)
            awaitComplete()
        }
    }

    @Test
    fun `brandColorsFlow always emits the Forest Network brand colour`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        defaultEnterpriseService.brandColorsFlow(null).test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(brandRest)
            awaitComplete()
        }
    }

    @Test
    fun `semanticColorsFlow always emits the Forest Network brand colours for a session`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        defaultEnterpriseService.semanticColorsFlow(A_SESSION_ID).test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(expectedSemanticColors)
            awaitComplete()
        }
    }

    @Test
    fun `overrideBrandColor has no effect`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        defaultEnterpriseService.overrideBrandColor(A_SESSION_ID, "aColor")
    }

    @Test
    fun `firebasePushGateway returns null`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.firebasePushGateway()).isNull()
    }

    @Test
    fun `unifiedPushDefaultPushGateway returns null`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.unifiedPushDefaultPushGateway()).isNull()
    }

    @Test
    fun `bugReportUrlFlow only emits Disabled`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        defaultEnterpriseService.bugReportUrlFlow(A_SESSION_ID).test {
            assertThat(awaitItem()).isEqualTo(BugReportUrl.Disabled)
            awaitComplete()
        }
    }

    @Test
    fun `getNoisyNotificationChannelId returns null`() = runTest {
        val defaultEnterpriseService = DefaultEnterpriseService()
        assertThat(defaultEnterpriseService.getNoisyNotificationChannelId(A_SESSION_ID)).isNull()
    }
}
