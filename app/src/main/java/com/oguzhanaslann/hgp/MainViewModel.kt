package com.oguzhanaslann.hgp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

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
