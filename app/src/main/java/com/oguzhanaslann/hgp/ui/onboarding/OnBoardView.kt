package com.oguzhanaslann.hgp.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.oguzhanaslann.hgp.R
import com.oguzhanaslann.hgp.ui.PagerIndicator
import com.oguzhanaslann.hgp.ui.theme.HGPTheme
import com.oguzhanaslann.hgp.ui.theme.contentPadding
import com.oguzhanaslann.hgp.ui.theme.jetStream
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingView(
    modifier: Modifier = Modifier,
    sysUiController: SystemUiController = rememberSystemUiController(),
    onSkip: () -> Unit = {}
) {
    val pagerState: PagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val backgroundColor = MaterialTheme.colors.background

    SideEffect {
        sysUiController.setNavigationBarColor(color = backgroundColor)
        sysUiController.setSystemBarsColor(color = backgroundColor)
    }

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxSize()
                .align(Alignment.TopCenter)
        ) {
            OnBoardPager(
                modifier = Modifier
                    .padding(top = contentPadding)
                    .fillMaxHeight(0.65f),
                pagerContent = listOf(
                    { StopReadingBrochureView() },
                    { ScanQrView() },
                    { SeeGreennessRatingView() },
                    { LiveGreenerView() }
                ),
                pagerState = pagerState
            )

            PagerIndicator(
                modifier = Modifier.padding(top = contentPadding),
                currentSelectionIndex = pagerState.currentPage,
                total = pagerState.pageCount
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
        ) {


            TextButton(
                modifier = Modifier
                    .align(Alignment.Center),
                onClick = { onSkip() },
            ) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.subtitle2,
                    color = jetStream
                )
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = contentPadding),
                backgroundColor = MaterialTheme.colors.primary,
                onClick = {
                    coroutineScope.launch {
                        val nextStep = pagerState.currentPage + 1

                        if (nextStep < pagerState.pageCount) {
                            pagerState.animateScrollToPage(nextStep , pagerState.currentPageOffset)
                        } else {
                            onSkip()
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_ios_24),
                    contentDescription = "Next"
                )
            }
        }
    }
}
@Composable
private fun LiveGreenerView() {
    OnboardPageView {
        Image(
            modifier = Modifier
                .size(width = 252.dp, height = 252.dp)
                .padding(top = 36.dp),
            painter = painterResource(id = R.drawable.live_greener_img),
            contentDescription = null
        )

        OnBoardTitle(
            text = "Live Greener!",
            modifier = Modifier.padding(bottom = 36.dp),
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun OnboardPageView(
    modifier: Modifier = Modifier,
    columnScope: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .border(
                color = Color.Black,
                width = 1.dp,
                shape = MaterialTheme.shapes.medium
            )
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        content = columnScope
    )
}

@Composable
private fun SeeGreennessRatingView() {
    OnboardPageView {
        Image(
            modifier = Modifier
                .size(width = 198.dp, height = 252.dp)
                .padding(top = 36.dp),
            painter = painterResource(id = R.drawable.list_image),
            contentDescription = null
        )

        OnBoardTitle(
            text = "See Greenness Ratings",
            modifier = Modifier.padding(bottom = 36.dp)
        )

    }
}

@Composable
private fun StopReadingBrochureView() {
    OnboardPageView {
        Image(
            modifier = Modifier.padding(top = 36.dp),
            painter = painterResource(id = R.drawable.ic_stop_read),
            contentDescription = null
        )

        OnBoardTitle(
            text = "Stop Reading Product Statistics",
            modifier = Modifier.padding(bottom = 36.dp)
        )

    }
}

@Composable
private fun ScanQrView() {
    OnboardPageView {
        Image(
            painter = painterResource(id = R.drawable.ic_capture),
            contentDescription = null
        )
        OnBoardTitle(
            text = "Scan your QR code",
        )

        Image(
            modifier = Modifier
                .size(width = 252.dp, height = 252.dp),
            painter = painterResource(id = R.drawable.barcode_image),
            contentDescription = null
        )
    }
}

@Composable
fun OnBoardTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    style: TextStyle = MaterialTheme.typography.subtitle1
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color
    )
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun OnBoardPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    pagerContent: List<@Composable () -> Unit> = emptyList()
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .background(Color.Transparent),
        count = pagerContent.size
    ) { page ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = contentPadding),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            pagerContent[page]()
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnBoardingViewPreview() {
    HGPTheme {
        OnBoardingView()
    }
}
