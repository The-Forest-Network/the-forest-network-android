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
import io.element.android.libraries.androidutils.json.JsonProvider
import io.element.android.libraries.core.extensions.mapCatchingExceptions
import io.element.android.libraries.core.uri.ensureProtocol
import io.element.android.libraries.matrix.api.ClientUrlContentFetcher
import io.element.android.libraries.matrix.api.TemporaryMatrixClientFactory
import io.element.android.libraries.matrix.api.core.SessionId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber

@ContributesBinding(AppScope::class)
class DefaultEnterpriseService(
    private val temporaryMatrixClientFactory: TemporaryMatrixClientFactory,
    private val jsonProvider: JsonProvider,
) : EnterpriseService {
    override suspend fun isEnterpriseUser(sessionId: SessionId) = false
    override suspend fun tweakMasUrl(url: String, urlContentFetcher: ClientUrlContentFetcher) = url
    override fun homeserverAllowList(): List<String> = listOf("https://matrix.theforestnetwork.earth")
    override suspend fun isAllowedToConnectToHomeserver(homeserverUrl: String) =
        homeserverUrl == "https://matrix.theforestnetwork.earth"
    override suspend fun isElementProEnforced(serverName: String): Boolean {
        val temporaryMatrixClient = temporaryMatrixClientFactory.create(serverName).getOrElse { return false }
        return temporaryMatrixClient.use { client ->
            val baseUrl = serverName.ensureProtocol().removeSuffix("/")
            // We'll always perform a network request here since we're not interested in any cached value.
            client.getUrl("$baseUrl/.well-known/element/element.json")
                .mapCatchingExceptions { response ->
                    val remoteConfig = jsonProvider().decodeFromString<MinimalEnterpriseConfig>(String(response))
                    remoteConfig.enforceElementPro ?: false
                }
                .onFailure {
                    Timber.e(it, "Failed to fetch enterprise config for checking if Element Pro is enforced for $serverName")
                }
                .getOrElse { false }
        }
    }

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

/**
 * A minimal version of the enterprise config that only contains the fields we need to check if Element Pro is enforced.
 */
@Serializable
private data class MinimalEnterpriseConfig(
    @SerialName("enforce_element_pro") val enforceElementPro: Boolean? = null,
)
