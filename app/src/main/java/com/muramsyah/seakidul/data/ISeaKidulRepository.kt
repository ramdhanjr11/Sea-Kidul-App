/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.data

import kotlinx.coroutines.flow.Flow

interface ISeaKidulRepository {
    suspend fun saveUiThemes(nightMode: Boolean)
    suspend fun saveLanguage(isEnglish: Boolean)
    suspend fun saveOnboardingState(onBoarding: Boolean)
    fun getUiThemes(): Flow<Boolean>
    fun getLanguage(): Flow<Boolean>
    fun getOnBoardingState(): Flow<Boolean>
}