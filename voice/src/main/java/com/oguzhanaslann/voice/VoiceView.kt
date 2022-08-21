package com.oguzhanaslann.voice

import androidx.compose.foundation.border
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
import androidx.compose.runtime.collectAsState
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

class VoiceViewModel : ViewModel() {
    private val _voiceState = MutableStateFlow<VoiceSearchUIState>(VoiceSearchUIState.Idle)
    val voiceState: StateFlow<VoiceSearchUIState>
        get() = _voiceState

    private val _voiceSearchProgress = MutableStateFlow<VoiceSearchProgressState?>(null)
    val voiceSearchProgress: StateFlow<VoiceSearchProgressState?>
        get() = _voiceSearchProgress

    val isListening = _voiceSearchProgress.map { it == VoiceSearchProgressState.Listening }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    fun loadHistory() {
        viewModelScope.launch {
            _voiceSearchProgress.emit(VoiceSearchProgressState.Loading)
            delay(2500)
            _voiceSearchProgress.emit(null)
            _voiceState.emit(VoiceSearchUIState.HistoryResult)
            delay(2500)
            _voiceSearchProgress.emit(VoiceSearchProgressState.Listening)
            delay(2500)
            _voiceState.emit(VoiceSearchUIState.SearchResult)
            _voiceSearchProgress.emit(VoiceSearchProgressState.Listening)
        }
    }

    fun startListening() {
        viewModelScope.launch {
            _voiceSearchProgress.emit(VoiceSearchProgressState.Listening)
        }
    }

    fun cancelListening() {
        viewModelScope.launch {
            _voiceSearchProgress.emit(null)
        }
    }


}

class VoiceSearchResult()

sealed class VoiceSearchUIState {
    object Idle : VoiceSearchUIState()
    object HistoryResult : VoiceSearchUIState()
    object SearchResult : VoiceSearchUIState()
}

sealed class VoiceSearchProgressState {
    object Listening : VoiceSearchProgressState()
    object Loading : VoiceSearchProgressState()
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                ) {
                    val voiceSearchUIState by voiceViewModel.voiceState.collectAsStateWithLifecycle()
                    when (voiceSearchUIState) {
                        VoiceSearchUIState.Idle -> voiceViewModel.loadHistory()
                        VoiceSearchUIState.HistoryResult -> SearchHistoryView(searchResults = emptyList())
                        VoiceSearchUIState.SearchResult -> SearchResultsView(searchResults = emptyList())
                    }
                }

                Column(modifier = Modifier.align(Alignment.Center)) {
                    val voiceSearchProgressState by voiceViewModel.voiceSearchProgress.collectAsStateWithLifecycle()
                    when (voiceSearchProgressState) {
                        VoiceSearchProgressState.Listening -> ListeningView()
                        VoiceSearchProgressState.Loading -> LoadingView()
                        null -> Unit
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
    Column(
        modifier = Modifier.padding(horizontal = defaultContentPadding),
        verticalArrangement = Arrangement.spacedBy(smallContentPadding),
    ) {
        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = stringResource(R.string.your_search_history),
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
fun SearchResultsView(searchResults: List<SearchResult>) {
    Column(
        modifier = Modifier.padding(horizontal = defaultContentPadding),
        verticalArrangement = Arrangement.spacedBy(smallContentPadding),
    ) {
        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = stringResource(R.string.results),
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primaryVariant,
                shape = MaterialTheme.shapes.small
            )
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
