/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.utils

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKey {
    const val PREFERENCE_APP_SEAKIDUL = "preferences_app"
    val ONBOARDING_KEY: Preferences.Key<Boolean> = booleanPreferencesKey("onboarding_key")
    val NIGHT_MODE_KEY: Preferences.Key<Boolean> = booleanPreferencesKey("night_mode_key")
    val LANGUAGE_KEY: Preferences.Key<Boolean> = booleanPreferencesKey("language_key")
}