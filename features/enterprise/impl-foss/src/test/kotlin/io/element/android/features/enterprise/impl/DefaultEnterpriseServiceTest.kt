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
import io.element.android.libraries.androidutils.json.JsonProvider
import io.element.android.libraries.core.uri.ensureProtocol
import io.element.android.libraries.matrix.test.A_HOMESERVER_URL
import io.element.android.libraries.matrix.test.A_SESSION_ID
import io.element.android.libraries.matrix.test.FakeTemporaryMatrixClient
import io.element.android.libraries.matrix.test.FakeTemporaryMatrixClientFactory
import io.element.android.tests.testutils.lambda.lambdaRecorder
import io.element.android.tests.testutils.lambda.value
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
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
    fun `homeserverAllowList returns the Forest Network homeserver`() {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        assertThat(defaultEnterpriseService.homeserverAllowList()).containsExactly("https://matrix.theforestnetwork.earth")
    }

    @Test
    fun `isAllowedToConnectToHomeserver is true only for the Forest Network homeserver`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        assertThat(defaultEnterpriseService.isAllowedToConnectToHomeserver("https://matrix.theforestnetwork.earth")).isTrue()
        assertThat(defaultEnterpriseService.isAllowedToConnectToHomeserver(A_HOMESERVER_URL)).isFalse()
    }

    @Test
    fun `isEnterpriseUser always return false`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        assertThat(defaultEnterpriseService.isEnterpriseUser(A_SESSION_ID)).isFalse()
    }

    @Test
    fun `semanticColorsFlow always emits the Forest Network brand colours`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        defaultEnterpriseService.semanticColorsFlow(null).test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(expectedSemanticColors)
            awaitComplete()
        }
    }

    @Test
    fun `brandColorsFlow always emits the Forest Network brand colour`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        defaultEnterpriseService.brandColorsFlow(null).test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(brandRest)
            awaitComplete()
        }
    }

    @Test
    fun `semanticColorsFlow always emits the Forest Network brand colours for a session`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        defaultEnterpriseService.semanticColorsFlow(A_SESSION_ID).test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(expectedSemanticColors)
            awaitComplete()
        }
    }

    @Test
    fun `overrideBrandColor has no effect`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        defaultEnterpriseService.overrideBrandColor(A_SESSION_ID, "aColor")
    }

    @Test
    fun `firebasePushGateway returns null`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        assertThat(defaultEnterpriseService.firebasePushGateway()).isNull()
    }

    @Test
    fun `unifiedPushDefaultPushGateway returns null`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        assertThat(defaultEnterpriseService.unifiedPushDefaultPushGateway()).isNull()
    }

    @Test
    fun `bugReportUrlFlow only emits Disabled`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        defaultEnterpriseService.bugReportUrlFlow(A_SESSION_ID).test {
            assertThat(awaitItem()).isEqualTo(BugReportUrl.Disabled)
            awaitComplete()
        }
    }

    @Test
    fun `getNoisyNotificationChannelId returns null`() = runTest {
        val defaultEnterpriseService = createDefaultEnterpriseService()
        assertThat(defaultEnterpriseService.getNoisyNotificationChannelId(A_SESSION_ID)).isNull()
    }

    @Test
    fun `isElementProEnforced checks using a temporary client`() = runTest {
        val closeLambda = lambdaRecorder<Unit> {}
        val getUrlLambda = lambdaRecorder<String, Result<ByteArray>> {
            Result.success("""{"enforce_element_pro": true}""".toByteArray())
        }
        val client = FakeTemporaryMatrixClient(
            getUrlResult = getUrlLambda,
            closeLambda = closeLambda,
        )
        val defaultEnterpriseService = createDefaultEnterpriseService(client = client)
        assertThat(defaultEnterpriseService.isElementProEnforced(A_HOMESERVER_URL)).isTrue()

        // Verify that the temporary client was used to fetch the URL and then closed
        val expectedUrl = "${A_HOMESERVER_URL.ensureProtocol()}/.well-known/element/element.json"
        getUrlLambda.assertions().isCalledOnce().with(value(expectedUrl))
        closeLambda.assertions().isCalledOnce()
    }

    private fun createDefaultEnterpriseService(
        client: FakeTemporaryMatrixClient = FakeTemporaryMatrixClient(),
        jsonProvider: JsonProvider = { Json }
    ) = DefaultEnterpriseService(
        temporaryMatrixClientFactory = FakeTemporaryMatrixClientFactory(createResult = { Result.success(client) }),
        jsonProvider = jsonProvider,
    )
}
