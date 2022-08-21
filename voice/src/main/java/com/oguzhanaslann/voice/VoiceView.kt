package com.oguzhanaslann.voice

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oguzhanaslann.commonui.*
import com.oguzhanaslann.commonui.theme.HGPExtendedTheme
import com.oguzhanaslann.commonui.theme.HGPTheme
import com.oguzhanaslann.commonui.theme.defaultContentPadding
import com.oguzhanaslann.commonui.theme.smallContentPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "VoiceView"

class VoiceViewModel : ViewModel() {
    private val _voiceState = MutableStateFlow(VoiceSearchUIState.Idle)
    val voiceState: StateFlow<VoiceSearchUIState>
        get() = _voiceState

    val isListening = _voiceState.map { it.progress.isListening }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun loadHistory() {
        viewModelScope.launch {
            _voiceState.value = _voiceState.value.copy(progress = Progress.loading())
            delay(1000)
            _voiceState.value = _voiceState.value.copy(
                progress = Progress.none(),
                searchResults = null,
                historyResults = listOf(
                    SearchResult(
                        id = "1",
                        productName = "Product 1",
                        producerName = "Producer 1",
                        imageUrl = "https://via.placeholder.com/150",
                    ),

                    SearchResult(
                        id = "2",
                        productName = "Product 2",
                        producerName = "Producer 2",
                        imageUrl = "https://via.placeholder.com/150",
                    ),

                    SearchResult(
                        id = "3",
                        productName = "Product 3",
                        producerName = "Producer 3",
                        imageUrl = "https://via.placeholder.com/150",
                    ),
                )
            )


        }
    }

    fun startListening() {
        viewModelScope.launch {
            _voiceState.value = _voiceState.value.copy(progress = Progress.listening())
        }
    }

    fun cancelListening() {
        viewModelScope.launch {
            _voiceState.value = _voiceState.value.copy(progress = Progress.none())
        }
    }


}

data class VoiceSearchUIState(
    val progress: Progress = Progress.none(),
    val historyResults: List<SearchResult>? = null,
    val searchResults: List<SearchResult>? = null
) {

    fun isIdle(
        skipCurrentProgress: Boolean = false
    ) = (progress == Progress.none() && !skipCurrentProgress) && historyResults == null && searchResults == null
    fun isHistory() = historyResults != null && searchResults == null
    fun isSearch() = historyResults == null && searchResults != null

    fun isListening() = progress.isListening
    fun isSearching() = progress.isLoading

    companion object {
        val Idle = VoiceSearchUIState()
    }
}

data class Progress(
    val isListening: Boolean,
    val isLoading: Boolean
) {

    init {
        require(!(isListening && isLoading)) { "Cannot have both listening and loading" }
    }

    companion object {
        fun none() = Progress(isListening = false, isLoading = false)
        fun loading() = Progress(isListening = false, isLoading = true)
        fun listening() = Progress(isListening = true, isLoading = false)
    }
}


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun VoiceView(
    modifier: Modifier = Modifier,
    voiceViewModel: VoiceViewModel = viewModel(),
    onBarcodeScanClicked: () -> Unit = {},
    onTextSearchClicked: () -> Unit = {},
    onVisualSearchClicked: () -> Unit = {},
    onPrivacyPolicyClicked: () -> Unit = {},
    onContactUsClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {}
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        bottomBar = {
            HGPBottomAppBar(
                onDrawerClick = { scope.launch { scaffoldState.drawerState.toggle() } }
            )
        },
        drawerShape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
        drawerContent = {
            HGPDrawer(
                onBarcodeScanClicked = onBarcodeScanClicked,
                onTextSearchClicked = onTextSearchClicked,
                onVisualSearchClicked = onVisualSearchClicked,
                onPrivacyPolicyClicked = onPrivacyPolicyClicked,
                onContactUsClicked = onContactUsClicked,
                onShareClicked = onShareClicked
            )
        },
        floatingActionButton = { VoiceSearchFAB(voiceViewModel) },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = { values ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(values)
            ) {
                val voiceSearchUIState by voiceViewModel.voiceState.collectAsStateWithLifecycle()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                ) {
                    when {
                        voiceSearchUIState.isIdle() -> voiceViewModel.loadHistory()
                        voiceSearchUIState.isHistory() -> SearchHistoryView(searchResults = voiceSearchUIState.historyResults!!)
                        voiceSearchUIState.isSearch() -> SearchResultsView(searchResults = emptyList())
                        else -> Log.e(TAG, "VoiceView: Unknown state")
                    }
                }

                Column(modifier = Modifier.align(Alignment.Center)) {
                    when {
                        voiceSearchUIState.isListening() -> ListeningView()
                        voiceSearchUIState.isSearching() -> LoadingView()
                        else -> Log.e(TAG, "VoiceView: Unknown state of progress")
                    }
                }
            }
        }
    )


}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
private fun VoiceSearchFAB(voiceViewModel: VoiceViewModel) {
    val isListening by voiceViewModel.isListening.collectAsStateWithLifecycle()
    if (isListening) {
        FloatingActionButton(onClick = voiceViewModel::cancelListening) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel voice search"
            )
        }
    } else {
        FloatingActionButton(onClick = voiceViewModel::startListening) {
            Icon(
                painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_microphone),
                contentDescription = "Start voice search"
            )
        }
    }
}


@Composable
fun ListeningView() {
    Blur(
        modifier = Modifier.fillMaxSize(),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(defaultContentPadding)
        ) {
            val (listeningText, pulsingButton) = createRefs()
            Text(
                modifier = Modifier.constrainAs(listeningText) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(pulsingButton.bottom, margin = 200.dp)
                },
                text = stringResource(R.string.listening),
                color = HGPExtendedTheme.blurColors.onBlur,
                style = MaterialTheme.typography.h5,
            )

            Box(
                modifier = Modifier.constrainAs(pulsingButton) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
            ) {
                val listeningButtonSize = 128.dp
                Pulsating(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Surface(
                        color = MaterialTheme.colors.secondary,
                        shape = CircleShape,
                        modifier = Modifier.size(listeningButtonSize),
                        content = {}
                    )
                }

                FloatingActionButton(
                    backgroundColor = MaterialTheme.colors.secondary,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(listeningButtonSize)
                        .align(Alignment.Center),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.size(54.dp),
                        painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_microphone),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun SearchHistoryView(
    searchResults: List<SearchResult>,
) {
    SearchResultsView(
        title = stringResource(R.string.your_search_history),
        searchResults = searchResults
    )
}

@Composable
fun SearchResultsView(
    title: String = stringResource(R.string.results),
    searchResults: List<SearchResult>
) {
    Column(
        modifier = Modifier.padding(horizontal = defaultContentPadding),
        verticalArrangement = Arrangement.spacedBy(smallContentPadding),
    ) {
        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = title,
            style = MaterialTheme.typography.subtitle1
        )

        LazyColumn {
            items(searchResults) { result ->
                SearchResultView(
                    searchResult = result,
                    onClick = { /* TODO */ }
                )

                Spacer(modifier = Modifier.height(smallContentPadding))
            }
        }
    }
}

@Composable
fun SearchResultView(
    modifier: Modifier = Modifier,
    searchResult: SearchResult,
    onClick: () -> Unit = {},
) {
    Surface(
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primaryVariant,
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShapeableImageView(
                modifier = Modifier
                    .padding(defaultContentPadding)
                    .size(56.dp),
                shape = MaterialTheme.shapes.small,
                painter = null,
            )

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    searchResult.productName,
                    style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp)
                )
                Text(
                    searchResult.producerName,
                    style = MaterialTheme.typography.overline
                )
            }

            Spacer(modifier = Modifier.weight(1f, fill = true))

            Surface(
                modifier = Modifier.padding(end = defaultContentPadding),
                shape = CircleShape
            ) {
                Icon(
                    modifier = Modifier.padding(smallContentPadding),
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun preview() {
    HGPTheme(darkTheme = false) {
        SearchResultView(
            modifier = Modifier.padding(horizontal = defaultContentPadding),
            searchResult = SearchResult(
                id = "1",
                productName = "Product Name",
                producerName = "Producer Name",
                imageUrl = "https://via.placeholder.com/56x56"
            ),
            onClick = {}
        )
    }
}


// preview
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewNoResult() {
    HGPTheme(darkTheme = false) {
        Surface {
            NoResultView(
                text = stringResource(id = R.string.empty_history_text)
            )
        }
    }
}

// search results preview
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewSearchResults() {
    HGPTheme {
        SearchHistoryView(
            searchResults = listOf(
                SearchResult(
                    id = "1",
                    productName = "Product Name",
                    producerName = "Producer Name",
                    imageUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
                ),
                SearchResult(
                    id = "2",
                    productName = "Product Name",
                    producerName = "Producer Name",
                    imageUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
                ),
                SearchResult(
                    id = "3",
                    productName = "Product Name",
                    producerName = "Producer Name",
                    imageUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
                ),
            )
        )
    }
}


//ListeningView preview
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewListeningView() {
    HGPTheme {
        ListeningView()
    }
}


// preview for voice search
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewVoiceSearch() {
    HGPTheme(darkTheme = false) {
        VoiceView()
    }
}
