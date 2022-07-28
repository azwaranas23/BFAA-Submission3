package com.azwar.githubuser.themes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.azwar.githubuser.settings.SettingPreferences
import kotlinx.coroutines.launch

class ThemeViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun setThemeMode(isNightMode: Boolean) {
        viewModelScope.launch { preferences.setThemeMode(isNightMode) }
    }

    fun getThemeMode(): LiveData<Boolean> {
        return preferences.getThemeMode().asLiveData()
    }
}