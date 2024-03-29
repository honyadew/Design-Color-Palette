package com.honyadew.designsystem.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.honyadew.GlobalSignals
import com.honyadew.designsystem.R
import com.honyadew.designsystem.theme.colorSelect
import com.honyadew.extencion.color
import com.honyadew.model.ColorInfo
import com.honyadew.model.CustomColorScheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RowToSave(
    colorsToSave: List<ColorInfo>,
    onSaveColorScheme: (colorScheme: CustomColorScheme) -> Unit,
    onRemoveFromToSaveList : (color : ColorInfo) -> Unit,
    modifier: Modifier = Modifier,
    showNameField: MutableState<Boolean> = remember{ mutableStateOf(false) },
    textNameField: MutableState<String> = remember{ mutableStateOf("") }
) {
    var clean by remember { mutableStateOf(false) }
    LaunchedEffect(clean){
        delay(600)
        textNameField.value = ""
        showNameField.value = false
    }

    AnimatedVisibility(
        modifier = modifier,
        visible =  colorsToSave.isNotEmpty(),
        enter = expandVertically(),
        exit = scaleOut()
    ) {
        Column(modifier = modifier.animateContentSize()) {
            Row(modifier = Modifier.padding(bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(width = 2.dp, color = colorSelect()),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        colorsToSave.forEach { colorInfo ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(colorInfo.value.color())
                                    .height(48.dp)
                                    .clickable { onRemoveFromToSaveList.invoke(colorInfo) }
                            )
                        }
                    }
                }
                val rotate = remember { Animatable(0f)}
                LaunchedEffect(showNameField.value){
                    rotate.animateTo(
                        targetValue = if (showNameField.value) 90f else 0f
                    )
                }
                IconButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showNameField.value = !showNameField.value
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_to_end),
                        contentDescription = "Arrow",
                        modifier = Modifier.graphicsLayer(rotationZ = rotate.value)
                    )
                }
            }
            if (showNameField.value){
                Row(modifier = Modifier.padding(bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = textNameField.value,
                        onValueChange = {newValue ->
                            if (newValue.length < 21) textNameField.value = newValue
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorSelect(saturation = 70),
                            unfocusedBorderColor = colorSelect(saturation = 90),
                            focusedLabelColor = colorSelect(saturation = 70),
                            cursorColor = colorSelect(saturation = 90, inverse = true)
                        ),
                        singleLine = true,
                        label = {
                            Text(text = stringResource(id = R.string.name_color_scheme))
                        }
                    )
                    IconButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onSaveColorScheme.invoke(
                                CustomColorScheme(
                                    colors = colorsToSave,
                                    name = textNameField.value,
                                )
                            )
                            GlobalSignals.snackbarHostState.tryEmit("Saved")
                            clean = !clean
                        }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_save_24), contentDescription = "Save")
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun RowToSavePreview() {
    RowToSave(
        colorsToSave = listOf(ColorInfo(value = Color.Blue.toString(), name = "")),
        onSaveColorScheme = {},
        onRemoveFromToSaveList = {}
    )
}