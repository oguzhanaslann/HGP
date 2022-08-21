package com.oguzhanaslann.commonui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanaslann.commonui.theme.defaultContentPadding
import com.oguzhanaslann.commonui.theme.largeContentPadding
import com.oguzhanaslann.commonui.theme.smallContentPadding
import com.oguzhanaslann.commonui.theme.xlargeContentPadding

@Composable
fun HGPDrawer(
    modifier: Modifier = Modifier,
    onBarcodeScanClicked: (() -> Unit)? = null,
    onVoiceSearchClicked: (() -> Unit)? = null,
    onTextSearchClicked: (() -> Unit)? = null,
    onVisualSearchClicked: (() -> Unit)? = null,
    onPrivacyPolicyClicked: (() -> Unit)? = null,
    onContactUsClicked: (() -> Unit)? = null,
    onShareClicked: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colors.primary
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(144.dp)
                                    .padding(top = xlargeContentPadding),
                                painter = painterResource(id = R.drawable.ic_hgp_logo),
                                contentDescription = "HGP Logo",
                            )

                            Text(
                                modifier = Modifier.padding(vertical = defaultContentPadding),
                                text = stringResource(id = R.string.app_name_long),
                                style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold)
                            )

                        }
                    }
                }

                item {
                    Column {

                        Text(
                            modifier = Modifier
                                .padding(top = smallContentPadding)
                                .padding(horizontal = largeContentPadding),
                            text = "Search",
                            style = MaterialTheme.typography.subtitle1,
                        )

                        if (onBarcodeScanClicked != null) {
                            DrawerItem(
                                painter = painterResource(id = R.drawable.ic_capture),
                                text = stringResource(R.string.barcode_scan),
                                onClick = onBarcodeScanClicked
                            )
                        }

                        if (onVoiceSearchClicked != null) {
                            DrawerItem(
                                painter = painterResource(id = R.drawable.ic_microphone),
                                text = stringResource(R.string.voice_search),
                                onClick = onVoiceSearchClicked
                            )
                        }


                        if (onTextSearchClicked != null) {
                            DrawerItem(
                                painter = painterResource(id = R.drawable.ic_pencil),
                                text = stringResource(R.string.text_search),
                                onClick = onTextSearchClicked
                            )
                        }

                        if (onVisualSearchClicked != null) {
                            DrawerItem(
                                painter = painterResource(id = R.drawable.ic_camera),
                                text = stringResource(R.string.visual_search),
                                onClick = onVisualSearchClicked
                            )
                        }
                    }
                }


                item { Divider(modifier = Modifier.fillMaxWidth(0.90f)) }

                item {
                    Column {
                        Text(
                            modifier = Modifier
                                .padding(top = smallContentPadding)
                                .padding(horizontal = largeContentPadding),
                            text = "General",
                            style = MaterialTheme.typography.subtitle1,
                        )

                        if (onPrivacyPolicyClicked != null) {
                            DrawerItem(
                                painter = painterResource(id = R.drawable.ic_info),
                                text = stringResource(R.string.privacy_policy),
                                onClick = onPrivacyPolicyClicked
                            )
                        }

                        if (onContactUsClicked != null) {
                            DrawerItem(
                                painter = painterResource(id = R.drawable.ic_mail),
                                text = stringResource(R.string.contact_us),
                                onClick = onContactUsClicked
                            )
                        }

                        if (onShareClicked != null) {
                            DrawerItem(
                                painter = painterResource(id = R.drawable.ic_social),
                                text = stringResource(R.string.share),
                                onClick = onShareClicked
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    text: String,
    contentColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(vertical = defaultContentPadding)
                .padding(start = largeContentPadding),
            painter = painter,
            contentDescription = contentDescription,
            tint = contentColor,
        )

        Text(
            modifier = Modifier.padding(horizontal = largeContentPadding),
            text = text,
            style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
            color = contentColor
        )
    }
}
