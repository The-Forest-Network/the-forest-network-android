/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2023-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.libraries.designsystem.atomic.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.element.android.compound.theme.ElementTheme
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.libraries.designsystem.theme.components.Text

/**
 * Page for onboarding screens, with content and optional footer.
 *
 * Ref: https://www.figma.com/file/o9p34zmiuEpZRyvZXJZAYL/FTUE?type=design&node-id=133-5427&t=5SHVppfYzjvkEywR-0
 * @param modifier Classical modifier.
 * @param renderBackground whether to render the background image or not.
 * @param contentAlignment horizontal alignment of the contents.
 * @param footer optional footer.
 * @param content main content.
 */
@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    renderBackground: Boolean = true,
    contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    footer: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // BG
        if (renderBackground) {
            OnBoardingBackground()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(all = 20.dp),
        ) {
            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = contentAlignment,
            ) {
                content()
            }
            // Footer
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                footer()
            }
        }
    }
}

@Composable
private fun OnBoardingBackground(modifier: Modifier = Modifier) {
    val isDark = !ElementTheme.isLightTheme
    val baseColor = if (isDark) Color.Black else Color.White
    // iOS MeshGradient mid-row colours — placed directly, no compositing
    val leftPlum = if (isDark) Color(0xFF532030) else Color(0xFF762E44)
    val centerAmber = if (isDark) Color(0xFF614722) else Color(0xFF8A6538)
    val rightGold = if (isDark) Color(0xFF897022) else Color(0xFFC4A030)
    Spacer(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(color = baseColor)
                // Horizontal gradient places each colour at its exact x-position —
                // no overlap, no compositing muddiness
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(leftPlum, centerAmber, rightGold),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                    )
                )
                // Vertical mask fades the colour band: slight tint at top, full colour
                // through the middle, fading to base at the bottom
                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to baseColor,
                            0.26f to Color.Transparent,
                            0.38f to Color.Transparent,
                            0.62f to baseColor,
                        )
                    )
                )
            }
    )
}

@PreviewsDayNight
@Composable
internal fun OnBoardingPagePreview() = ElementPreview {
    OnBoardingPage(
        content = {
            Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Content",
                    style = ElementTheme.typography.fontHeadingXlBold
                )
            }
        },
        footer = {
            Box(
                Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Footer",
                    style = ElementTheme.typography.fontHeadingXlBold
                )
            }
        }
    )
}
