/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.muramsyah.seakidul.utils.PreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeaKidulRepository @Inject constructor(private val dataStore: DataStore<Preferences>) : ISeaKidulRepository {

    override suspend fun saveUiThemes(nightMode: Boolean) {
        dataStore.edit { preference ->
            preference[PreferencesKey.NIGHT_MODE_KEY] = nightMode
        }
    }

    override suspend fun saveLanguage(isEnglish: Boolean) {
        dataStore.edit { preference ->
            preference[PreferencesKey.LANGUAGE_KEY] = isEnglish
        }
    }

    override suspend fun saveOnboardingState(onBoarding: Boolean) {
        dataStore.edit { preference ->
            preference[PreferencesKey.ONBOARDING_KEY] = onBoarding
        }
    }

    override fun getUiThemes(): Flow<Boolean> = dataStore.data.map { preference ->
        preference[PreferencesKey.NIGHT_MODE_KEY] ?: false
    }

    override fun getLanguage(): Flow<Boolean> = dataStore.data.map { preference ->
        preference[PreferencesKey.LANGUAGE_KEY] ?: false
    }

    override fun getOnBoardingState(): Flow<Boolean> = dataStore.data.map { preference ->
        preference[PreferencesKey.ONBOARDING_KEY] ?: false
    }

}