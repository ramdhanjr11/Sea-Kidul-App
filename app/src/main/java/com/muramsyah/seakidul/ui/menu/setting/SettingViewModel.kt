/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.menu.setting

import androidx.lifecycle.*
import com.muramsyah.seakidul.data.SeaKidulRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val seaKidulRepository: SeaKidulRepository) :
    ViewModel() {

    private var _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    private var _isEnglish = MutableLiveData<Boolean>()
    val isEnglish: LiveData<Boolean> = _isEnglish

    init {
        getUiThemes()
        getLanguage()
    }

    private fun getUiThemes() {
        viewModelScope.launch {
            seaKidulRepository.getUiThemes().collect {
                _isDarkMode.value = it
            }
        }
    }

    fun setDarkMode() {
        val darkMode = !isDarkMode.value!!
        _isDarkMode.value = darkMode

        viewModelScope.launch {
            seaKidulRepository.saveUiThemes(darkMode)
        }
    }

    private fun getLanguage() {
        viewModelScope.launch {
            seaKidulRepository.getLanguage().collect {
                _isEnglish.value = it
            }
        }
    }

    fun setEnglishLanguage() {
        val english = !isEnglish.value!!
        _isEnglish.value =  english

        viewModelScope.launch {
            seaKidulRepository.saveLanguage(english)
        }
    }
}