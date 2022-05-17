/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.muramsyah.seakidul.data.SeaKidulRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val seaKidulRepository: SeaKidulRepository): ViewModel() {

    fun savingOnBoardingState() {
        viewModelScope.launch {
            seaKidulRepository.saveOnboardingState(true)
        }
    }

    fun getOnBoardingState() = seaKidulRepository.getOnBoardingState().asLiveData()

}