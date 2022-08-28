package com.oguzhanaslann.voice

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oguzhanaslann.commonui.*
import com.oguzhanaslann.commonui.theme.HGPExtendedTheme
import com.oguzhanaslann.commonui.theme.HGPTheme
import com.oguzhanaslann.commonui.theme.defaultContentPadding
import com.oguzhanaslann.commonui.theme.smallContentPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "VoiceView"

class VoiceSearchState(
    val voiceViewModel: VoiceViewModel,
    val scaffoldState: ScaffoldState,
    val scope: CoroutineScope
) {
    val voiceState
        get() = voiceViewModel.voiceState

    fun toggleDrawer() {
        scope.launch { scaffoldState.drawerState.toggle() }
    }

    fun stopListening() {
        voiceViewModel.stopListening()
    }

    fun startListening() {
        voiceViewModel.startListening()
    }

    fun searchForText(voiceInput: String?) {
        voiceViewModel.searchForText(voiceInput)
    }
}

@Composable
fun rememberVoiceSearchState(
    voiceViewModel: VoiceViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    scope: CoroutineScope = rememberCoroutineScope()
) = remember(voiceViewModel, scaffoldState, scope) {
    VoiceSearchState(voiceViewModel, scaffoldState, scope)
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
    val voiceSearchState = rememberVoiceSearchState(voiceViewModel)

    val voiceInputContract = rememberLauncherForActivityResult(GetVoiceInputContract()) {
        Log.e(TAG, "VoiceView: voiceInputContract: $it")
        voiceSearchState.stopListening()
        voiceSearchState.searchForText(it)
    }

    Scaffold(
        modifier = modifier,
        scaffoldState = voiceSearchState.scaffoldState,
        bottomBar = {
            HGPBottomAppBar(
                onDrawerClick = { voiceSearchState.toggleDrawer() },
            )
        },
        drawerShape = MaterialTheme.shapes.drawerShape,
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
        floatingActionButton = { VoiceSearchFAB(voiceSearchState.voiceViewModel) },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = { values ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(values)
            ) {
                val voiceSearchUIState by voiceSearchState.voiceState.collectAsStateWithLifecycle()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                ) {
                    when {
                        voiceSearchUIState.isIdle() -> voiceSearchState.voiceViewModel.loadHistory()
                        voiceSearchUIState.isHistory() ->
                            SearchHistoryView(searchResults = voiceSearchUIState.historyResults!!)
                        voiceSearchUIState.isSearch() ->
                            SearchResultsView(searchResults = voiceSearchUIState.searchResults!!)
                        else -> Log.e(TAG, "VoiceView: Unknown state")
                    }
                }

                Column(modifier = Modifier.align(Alignment.Center)) {
                    when {
                        voiceSearchUIState.isListening() -> ListeningView(onClick = {
                            voiceInputContract.launch(Unit)
                            voiceSearchState.startListening()
                        })
                        voiceSearchUIState.isSearching() -> LoadingView()
                        voiceSearchUIState.isNoProgress() -> Unit
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
    val state by voiceViewModel.voiceState.collectAsStateWithLifecycle()
    val isListening = state.isListening()
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
fun ListeningView(onClick: () -> Unit = {}) {
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
                text = stringResource(R.string.tap_below_to_start_listening),
                color = HGPExtendedTheme.blurColors.onBlur,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
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
                        content = emptyComposable
                    )
                }

                FloatingActionButton(
                    backgroundColor = MaterialTheme.colors.secondary,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(listeningButtonSize)
                        .align(Alignment.Center),
                    onClick = onClick
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
fun previewSearchResultView() {
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
