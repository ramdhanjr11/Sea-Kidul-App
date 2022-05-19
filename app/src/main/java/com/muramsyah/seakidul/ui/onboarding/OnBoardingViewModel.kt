/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.onboarding

import androidx.lifecycle.*
import com.muramsyah.seakidul.data.SeaKidulRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val seaKidulRepository: SeaKidulRepository): ViewModel() {

    private var _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    init {
        getUiThemes()
    }

    fun savingOnBoardingState() {
        viewModelScope.launch {
            seaKidulRepository.saveOnboardingState(true)
        }
    }

    fun getOnBoardingState() = seaKidulRepository.getOnBoardingState().asLiveData()

    private fun getUiThemes() {
        viewModelScope.launch {
            seaKidulRepository.getUiThemes().collect {
                _isDarkMode.value = it
            }
        }
    }
}