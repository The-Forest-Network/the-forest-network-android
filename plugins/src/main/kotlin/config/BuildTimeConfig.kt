/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package config

object BuildTimeConfig {
    const val APPLICATION_ID = "earth.theforestnetwork.village.android"
    const val APPLICATION_NAME = "The Forest Network"
    // Firebase project: the-forest-network-baef8.
    const val GOOGLE_APP_ID_RELEASE = "1:730998879297:android:6bd20f1b51524b2627c2c0"
    const val GOOGLE_APP_ID_DEBUG = "1:730998879297:android:e65718b849c90aa127c2c0"
    const val GOOGLE_APP_ID_NIGHTLY = "1:730998879297:android:b8593ef1d9d2b03627c2c0"

    val METADATA_HOST_REVERSED: String? = null
    val OAUTH_CLIENT_URL_PATH: String? = "apps/android"
    val URL_WEBSITE: String? = "https://theforestnetwork.earth"
    val URL_LOGO: String? = null
    val URL_COPYRIGHT: String? = "https://theforestnetwork.earth/copyright"
    val URL_ACCEPTABLE_USE: String? = "https://theforestnetwork.earth/acceptable-use"
    val URL_PRIVACY: String? = "https://theforestnetwork.earth/privacy"
    val URL_POLICY: String? = "https://theforestnetwork.earth/privacy"
    val URL_REQUEST_ACCOUNT: String? = "https://theforestnetwork.earth/village/join"
    val SERVICES_MAPTILER_BASE_URL: String? = null
    val SERVICES_MAPTILER_APIKEY: String? = null
    val SERVICES_MAPTILER_LIGHT_MAPID: String? = null
    val SERVICES_MAPTILER_DARK_MAPID: String? = null
    val SERVICES_POSTHOG_HOST: String? = null
    val SERVICES_POSTHOG_APIKEY: String? = null
    val SERVICES_SENTRY_DSN: String? = null
    val SERVICES_SENTRY_DSN_RUST: String? = null
    val BUG_REPORT_URL: String? = null
    val BUG_REPORT_APP_NAME: String? = null
    const val PUSH_CONFIG_INCLUDE_FIREBASE: Boolean = true
    const val PUSH_CONFIG_INCLUDE_UNIFIED_PUSH: Boolean = true
    val PUSHER_APP_ID_RELEASE: String? = null
    val PUSHER_APP_ID_DEBUG: String? = null
    val PUSHER_APP_ID_NIGHTLY: String? = null
}
