/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2024, 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.enterprise.impl

import androidx.compose.ui.graphics.Color
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import io.element.android.compound.colors.SemanticColorsLightDark
import io.element.android.compound.tokens.generated.compoundColorsDark
import io.element.android.compound.tokens.generated.compoundColorsLight
import io.element.android.features.enterprise.api.BugReportUrl
import io.element.android.features.enterprise.api.EnterpriseService
import io.element.android.libraries.matrix.api.core.SessionId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@ContributesBinding(AppScope::class)
class DefaultEnterpriseService : EnterpriseService {
    override val isEnterpriseBuild = false

    override suspend fun isEnterpriseUser(sessionId: SessionId) = false
    override suspend fun tweakMasUrl(url: String, homeserver: String) = url
    override fun defaultHomeserverList(): List<String> = listOf("https://matrix.theforestnetwork.earth")
    override suspend fun isAllowedToConnectToHomeserver(homeserverUrl: String) =
        homeserverUrl == "https://matrix.theforestnetwork.earth"

    override suspend fun overrideBrandColor(sessionId: SessionId?, brandColor: String?) = Unit

    override fun brandColorsFlow(sessionId: SessionId?): Flow<Color?> {
        return flowOf(Color(0xFF535946))
    }

    override fun semanticColorsFlow(sessionId: SessionId?): Flow<SemanticColorsLightDark> {
        val brandRest = Color(0xFF535946)
        val brandHovered = Color(0xFF3D4234)
        val brandPressed = Color(0xFF2A2D24)
        val brandSubtle = Color(0xFF6B7060)
        val brandSelected = Color(0x33535946)
        return flowOf(
            SemanticColorsLightDark(
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
        )
    }

    override fun firebasePushGateway(): String? = null
    override fun unifiedPushDefaultPushGateway(): String? = null

    override fun bugReportUrlFlow(sessionId: SessionId?): Flow<BugReportUrl> {
        return flowOf(BugReportUrl.Disabled)
    }

    override fun getNoisyNotificationChannelId(sessionId: SessionId): String? = null
}
