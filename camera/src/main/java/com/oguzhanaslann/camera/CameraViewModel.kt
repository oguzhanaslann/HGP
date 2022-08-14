package com.oguzhanaslann.camera

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(

) : ViewModel() {

    var scanState by  mutableStateOf<ScanState>(ScanState.Idle)
}
