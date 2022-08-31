package com.oguzhanaslann.textsearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oguzhanaslann.commonui.modalBottomSheetShape
import com.oguzhanaslann.commonui.theme.HGPTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextView(
    modifier: Modifier = Modifier,
    onBarcodeScanClicked: (() -> Unit)? = null,
    onVoiceSearchClicked: (() -> Unit)? = null,
    onVisualSearchClicked: (() -> Unit)? = null,
    onPrivacyPolicyClicked: (() -> Unit)? = null,
    onContactUsClicked: (() -> Unit)? = null,
    onShareClicked: (() -> Unit)? = null
) {
    val scaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = scaffoldState,
        sheetShape = MaterialTheme.shapes.modalBottomSheetShape,
        content = {
            Button(onClick = { coroutineScope.launch { scaffoldState.show() } }) {
                Text("content")
            }
        },
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Button(onClick = { coroutineScope.launch { scaffoldState.hide() } }) {
                    Text("collapse")
                }
            }
        }
    )
}

@Composable
fun SearchButton(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .height(56.dp)
            .width(64.dp)
            .then(modifier),
        onClick = onClicked,
        shape = MaterialTheme.shapes.medium,
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = MaterialTheme.colors.onPrimary
        )
    }
}

@Preview
@Composable
fun previewSearchButton() {
    HGPTheme {
        SearchButton()
    }
}


@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    hint: String,
    value: String,
    onValueChanged: (String) -> Unit = {},
    onClearTextClicked:() -> Unit = {} ,
    onSearchClicked: () -> Unit = {},
    onSettingsClicked: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 64.dp)
                .padding(end = 64.dp)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            val textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start)

            @OptIn(ExperimentalMaterialApi::class)
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value,
                onValueChange = onValueChanged,
                singleLine = true,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                interactionSource = interactionSource,
                textStyle = textStyle,
                decorationBox = @Composable { innerTextField ->
                    TextFieldDefaults.TextFieldDecorationBox(
                        value = value,
                        visualTransformation = visualTransformation,
                        innerTextField = innerTextField,
                        placeholder = {
                            Text(
                                text = hint,
                                style = textStyle
                            )
                        },
                        trailingIcon = @Composable {
                            AnimatedVisibility(
                                visible = value.isNotEmpty(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(
                                    onClick = onClearTextClicked
                                ) {
                                    Icon(
                                        modifier = modifier.size(20.dp),
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        interactionSource = interactionSource,
                        singleLine = true,
                        enabled = true,
                        isError = false,
                        colors = colors
                    )
                }
            )
        }

        SearchButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClicked = onSearchClicked
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier
                    .height(56.dp)
                    .width(1.dp),
                color = MaterialTheme.colors.primary
            )
            IconButton(
                modifier = Modifier.padding(end = 4.dp),
                onClick = onSettingsClicked
            ) {
                Icon(
                    painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_settings),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun previewSearchTextField() {
    HGPTheme {
        var input by remember { mutableStateOf("") }
        SearchTextField(hint = "hint", value = input, onValueChanged = {input = it }, onClearTextClicked = { input = "" })
    }
}
