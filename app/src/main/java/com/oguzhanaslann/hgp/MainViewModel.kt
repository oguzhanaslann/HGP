package com.oguzhanaslann.hgp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.commonui.base.SearchContent
import com.oguzhanaslann.hgp.domain.SearchContentProvider
import com.oguzhanaslann.hgp.ui.main.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchContentProvider: SearchContentProvider
) : ViewModel() {

    val isInitializing = MutableLiveData<Boolean>(true)

    val searchType = MutableStateFlow(initialSearchType)
    val searchContent = searchType.map { it.toSearchContent() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(fiveSecondsStopTimeoutMillis),
            initialValue = searchContentProvider.getSearchContentBy(initialSearchType)
        )

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

    /**
    *  using directly navigation components may be better but IDK
    * */
    private fun SearchType.toSearchContent(): SearchContent<ViewModel> {
        return searchContentProvider.getSearchContentBy(this)
    }

    companion object {
        val initialSearchType = SearchType.QRScanSearch
        const val fiveSecondsStopTimeoutMillis = 5000L
    }
}
