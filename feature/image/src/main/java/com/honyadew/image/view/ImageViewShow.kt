package com.honyadew.image.view

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.ImageColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.honyadew.GlobalSignals
import com.honyadew.image.R
import com.honyadew.extencion.extractColors
import com.honyadew.designsystem.view.DcpSlider
import com.honyadew.designsystem.view.RowToSave
import com.honyadew.image.contract.ImageState
import com.honyadew.designsystem.theme.colorSelect
import com.honyadew.designsystem.view.MiniColorCard
import com.honyadew.extencion.color
import com.honyadew.extencion.string
import com.honyadew.extencion.toHexString
import com.honyadew.extencion.toStringRGBA
import com.honyadew.model.ColorInfo
import com.honyadew.model.ColorSchemeSource
import com.honyadew.model.CustomColorScheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageViewShow(
    state: ImageState.Show,
    onSaveColorScheme: (colorScheme : CustomColorScheme) -> Unit,
    onRemoveFromToSave: (colorInfo: ColorInfo) -> Unit,
    onMoveToSave: (colorInfo: ColorInfo) -> Unit,
    onChangeSelectedColor: (color: Color) -> Unit,
    onExtractColor: (extractedColors: List<ColorInfo>) -> Unit,
    onSetBitmap: (bitmap: Bitmap)-> Unit,
    activity: Activity = LocalContext.current as Activity,
    pickerController: ColorPickerController = rememberColorPickerController(),
    alphaValueState: MutableState<Float> = remember{ mutableStateOf(1f)}
) {
    val portraitMode: Boolean =
        (LocalContext.current as Activity)
            .resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val showDropDownMenu = remember{ mutableStateOf(false)}

    LaunchedEffect(pickerController.selectedColor.value, alphaValueState.value){
        onChangeSelectedColor.invoke(
            pickerController.selectedColor.value.copy(alpha = alphaValueState.value)
        )
    }

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val bitmap = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(activity.contentResolver, uri)
                )
                val compatibleBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)
                onExtractColor.invoke(compatibleBitmap.extractColors())
                onSetBitmap.invoke(compatibleBitmap)
                pickerController.setPaletteImageBitmap(bitmap.asImageBitmap())
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()){

        if (portraitMode) {
            PortraitImageViewShow(
                state = state,
                onSaveColorScheme = onSaveColorScheme,
                onRemoveFromToSave = onRemoveFromToSave,
                onMoveToSave = onMoveToSave,
                onChangeSelectedColor = onChangeSelectedColor,
                onOpenDropDownMenu = {showDropDownMenu.value = it},
                showDropDownMenu = showDropDownMenu.value,
                pickerLauncher = pickerLauncher,
                alphaValueState = alphaValueState,
                pickerController = pickerController
            )
        } else{
            LandscapeImageViewShow(
                state = state,
                onSaveColorScheme = onSaveColorScheme,
                onRemoveFromToSave = onRemoveFromToSave,
                onMoveToSave = onMoveToSave,
                onChangeSelectedColor = onChangeSelectedColor,
                onOpenDropDownMenu = {showDropDownMenu.value = it},
                showDropDownMenu = showDropDownMenu.value,
                pickerLauncher = pickerLauncher,
                alphaValueState = alphaValueState,
                pickerController = pickerController
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .padding(top = 20.dp)
                .align(
                    if (portraitMode) Alignment.TopStart else Alignment.TopEnd
                ),
            visible = showDropDownMenu.value,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            DropDownMenu(
                extractedColors = state.extractedColors,
                onSaveColorScheme = { colorScheme ->
                    onSaveColorScheme.invoke(colorScheme)
                    showDropDownMenu.value = false
                },
                onCloseDropDownMenu = {showDropDownMenu.value = false}
            )
        }

    }
}

@Composable
fun PortraitImageViewShow(
    state: ImageState.Show,
    showDropDownMenu : Boolean,
    onSaveColorScheme: (colorScheme : CustomColorScheme) -> Unit,
    onRemoveFromToSave: (colorInfo: ColorInfo) -> Unit,
    onMoveToSave: (colorInfo: ColorInfo) -> Unit,
    onChangeSelectedColor: (color: Color) -> Unit,
    onOpenDropDownMenu : (open: Boolean) -> Unit,
    pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    alphaValueState: MutableState<Float>,
    pickerController: ColorPickerController
) {
    Column {
        if (state.extractedColors.isNotEmpty()){
            TabOfDropDownMenu(
                showDropDownMenu = showDropDownMenu,
                extractedColors = state.extractedColors,
                onSaveColorScheme = onSaveColorScheme,
                onOpenDropDownMenu = onOpenDropDownMenu
            )
        }
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = colorSelect()),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                OutlinedCard(
                    modifier = Modifier.padding(bottom = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(width = 2.dp, color = colorSelect()),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    ImageColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        controller = pickerController,
                        paletteImageBitmap = state.imageBitmap?.asImageBitmap()?: ImageBitmap
                            .imageResource(id = R.drawable.pelican_image)
                    )
                }
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    RowToSave(
                        colorsToSave = state.colorsToSave,
                        onSaveColorScheme = onSaveColorScheme,
                        onRemoveFromToSaveList = onRemoveFromToSave
                    )
                    FunctionalRow(
                        modifier = Modifier.fillMaxHeight(),
                        selectedColor = state.selectedColor,
                        alphaValueState = alphaValueState,
                        onMoveToSave = onMoveToSave,
                        pickerLauncher = pickerLauncher
                    )
                }
            }
        }
    }
}


@Composable
fun LandscapeImageViewShow(
    state: ImageState.Show,
    showDropDownMenu: Boolean,
    onSaveColorScheme: (colorScheme: CustomColorScheme) -> Unit,
    onRemoveFromToSave: (colorInfo: ColorInfo) -> Unit,
    onMoveToSave: (colorInfo: ColorInfo) -> Unit,
    onChangeSelectedColor: (color: Color) -> Unit,
    onOpenDropDownMenu: (open: Boolean) -> Unit,
    pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    alphaValueState: MutableState<Float>,
    pickerController: ColorPickerController
) {
    Row {
        OutlinedCard(
            modifier = Modifier.padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(width = 2.dp, color = colorSelect()),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            ImageColorPicker(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                controller = pickerController,
                paletteImageBitmap = state.imageBitmap?.asImageBitmap()?: ImageBitmap
                    .imageResource(id = R.drawable.pelican_image)
            )
        }
        Column {
            if (state.extractedColors.isNotEmpty()){
                TabOfDropDownMenu(
                    showDropDownMenu = showDropDownMenu,
                    extractedColors = state.extractedColors,
                    onSaveColorScheme = onSaveColorScheme,
                    onOpenDropDownMenu = onOpenDropDownMenu
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = colorSelect()),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    RowToSave(
                        colorsToSave = state.colorsToSave,
                        onSaveColorScheme = onSaveColorScheme,
                        onRemoveFromToSaveList = onRemoveFromToSave
                    )

                    FunctionalRow(
                        modifier = Modifier.fillMaxHeight(),
                        selectedColor = state.selectedColor,
                        alphaValueState = alphaValueState,
                        onMoveToSave = onMoveToSave,
                        pickerLauncher = pickerLauncher
                    )
                }
            }
        }
    }
}

@Composable
private fun FunctionalRow(
    selectedColor: Color,
    alphaValueState: MutableState<Float>,
    onMoveToSave: (colorInfo: ColorInfo) -> Unit,
    pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    modifier: Modifier = Modifier,
    clipboardManager: ClipboardManager = LocalClipboardManager.current
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = {
                    val value = selectedColor.copy(alpha = alphaValueState.value).toStringRGBA()
                    clipboardManager.setText(AnnotatedString(buildString {
                        append(value)
                    }))
                    GlobalSignals.snackbarHostState.tryEmit("Copied: $value")

                },
                colors = ButtonDefaults.buttonColors(containerColor = colorSelect(saturation = 90)),
                modifier = Modifier
                    .weight(0.6f)
                    .widthIn(max = (256.dp))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "RGBA")
                    Text(text = selectedColor.copy(alpha = alphaValueState.value)
                        .toStringRGBA(), maxLines = 1)
                }
            }
            Spacer(modifier = Modifier.weight(0.05f))
            Button(
                onClick = {
                    val value = selectedColor.copy(alpha = alphaValueState.value).toHexString()
                    clipboardManager.setText(AnnotatedString(buildString {
                        append(value)
                    }))
                    GlobalSignals.snackbarHostState.tryEmit("Copied: $value")
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorSelect(saturation = 90)),
                modifier = Modifier
                    .weight(0.6f)
                    .widthIn(max = (256.dp))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "HEX")
                    Text(text = selectedColor.copy(alpha = alphaValueState.value)
                        .toHexString(), maxLines = 1)
                }
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(0.3f),
                onClick = {
                    onMoveToSave.invoke(
                        ColorInfo(
                            value = selectedColor.copy(alpha = alphaValueState.value).string(),
                            name = ""
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorSelect(saturation = 90))
            ) {
                Text(text = "ToSave")
            }
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 2.dp, color = colorSelect()),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .background(selectedColor.copy(alpha = alphaValueState.value))
                )
            }
            Button(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(0.3f),
                onClick = {
                    pickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorSelect(saturation = 90))
            ) {
                Text(text = "New Image")
            }
        }
        DcpSlider(
            value = alphaValueState.value,
            onValueChange = { newValue ->
                alphaValueState.value = newValue
            },
            steps = 100,
            color = colorSelect(saturation = 80, inverse = true),
            fullName = "Alpha"
        )
    }
}

@Composable
fun TabOfDropDownMenu(
    showDropDownMenu : Boolean,
    extractedColors: List<ColorInfo>,
    onSaveColorScheme: (colorScheme: CustomColorScheme) -> Unit,
    onOpenDropDownMenu : (open: Boolean) -> Unit,
    modifier : Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = colorSelect(saturation = 90))
    ) {
        Row {
            val rotate = remember { Animatable(0f) }
            LaunchedEffect(showDropDownMenu){
                rotate.animateTo(
                    targetValue = if (showDropDownMenu) 90f else 0f
                )
            }
            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = {
                    onOpenDropDownMenu.invoke(!showDropDownMenu)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_to_end),
                    contentDescription = "Arrow",
                    modifier = Modifier.graphicsLayer(rotationZ = rotate.value)
                )
            }
            OutlinedCard(
                modifier = Modifier.weight(0.6f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 2.dp, color = colorSelect()),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    extractedColors.forEach { colorInfo ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(colorInfo.value.color())
                                .height(48.dp)
                        )
                    }
                }
            }
            val basicName = stringResource(id = R.string.extracted_colors)

            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = {
                    onSaveColorScheme.invoke(
                        CustomColorScheme(
                            colors = extractedColors,
                            name = basicName,
                            source = ColorSchemeSource.ExtractAuto
                        )
                    )
                }
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_save_24), contentDescription = "Save")
            }
        }
    }
}

@Composable
fun DropDownMenu(
    modifier: Modifier = Modifier,
    extractedColors: List<ColorInfo>,
    onSaveColorScheme: (colorScheme: CustomColorScheme) -> Unit,
    onCloseDropDownMenu: () -> Unit,
    textNameField: MutableState<String> = remember{ mutableStateOf("") },
    focusRequester: FocusRequester = FocusRequester(),
) {
    OutlinedCard(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 32.dp)
            .widthIn(max = 512.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 2.dp, color = colorSelect()),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = colorSelect(saturation = 90))
    )  {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(4.dp)
        ) {
            val firstRow = extractedColors.slice(0 until extractedColors.size/2)
            val secondRow = extractedColors.slice(extractedColors.size/2 until extractedColors.size)
            Row() {
                firstRow.forEach { extract->
                    MiniColorCard(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        color = extract,
                        hexFontWeight = 12.sp
                    )
                }
            }
            Row() {
                secondRow.forEach { extract->
                    MiniColorCard(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        color = extract,
                        hexFontWeight = 12.sp

                    )
                }
            }
            Row(modifier = Modifier.padding(bottom = 4.dp, start = 4.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {

                OutlinedTextField(
                    value = textNameField.value,
                    onValueChange = {newValue ->
                        if (newValue.length < 21) textNameField.value = newValue
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorSelect(saturation = 70),
                        unfocusedBorderColor = colorSelect(saturation = 80),
                        focusedLabelColor = colorSelect(saturation = 70),
                        cursorColor = colorSelect(saturation = 80, inverse = true)
                    ),
                    singleLine = true,
                    label = {
                        Text(text = stringResource(id = R.string.name_color_scheme))
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions (
                        onDone = {
                            onSaveColorScheme.invoke(
                                CustomColorScheme(
                                    colors = extractedColors,
                                    name = textNameField.value,
                                    source = ColorSchemeSource.ExtractAuto
                                )
                            )
                        },
                    )
                )
                IconButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSaveColorScheme.invoke(
                            CustomColorScheme(
                                colors = extractedColors,
                                name = textNameField.value,
                                source = ColorSchemeSource.ExtractAuto
                            )
                        )
                    }
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_save_24), contentDescription = "Save")
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        delay(500)

    }
    BackHandler {
        onCloseDropDownMenu.invoke()
    }
}

