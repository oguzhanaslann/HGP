package com.oguzhanaslann.hgp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    val isInitializing = MutableLiveData<Boolean>(true)

    private val _isDoneOnBoarding = MutableStateFlow<Boolean>(false)
    val isDoneOnBoarding: StateFlow<Boolean>
        get() = _isDoneOnBoarding

    fun getIsDoneOnboarding() {
        _isDoneOnBoarding.value = false
        isInitializing.value = false
    }

    fun onSkipOnBoarding() {
        _isDoneOnBoarding.value = true
    }
}
