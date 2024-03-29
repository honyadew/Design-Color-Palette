package com.honyadew.designsystem.view

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.honyadew.GlobalSignals
import com.honyadew.designsystem.R
import com.honyadew.designsystem.theme.colorSelect
import com.honyadew.extencion.color
import com.honyadew.extencion.string
import com.honyadew.extencion.toHexString
import com.honyadew.model.ColorInfo
import com.honyadew.model.ColorOfMaterial
import com.honyadew.model.Palette

@Composable
fun DcpColorCard(
    color: ColorInfo,
    onColorClick: (color: ColorInfo) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteColor : ((color: ColorInfo) -> Unit)? = null,
    clipboardManager: ClipboardManager = LocalClipboardManager.current
) {
    val dAnimatable = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(Unit) {
        dAnimatable.animateTo(
            targetValue = 1f,
        )
    }

    Card(
        modifier = modifier
            .defaultMinSize(minWidth = 96.dp)
            .graphicsLayer {
                scaleY = dAnimatable.value
                scaleX = dAnimatable.value
            },
        colors = CardDefaults.cardColors(containerColor = colorSelect(90)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 128.dp)
                .clickable { onColorClick.invoke(color) },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(width = 2.dp, color = colorSelect()),
            colors = CardDefaults.cardColors(containerColor = color.value.color()),
            elevation = CardDefaults.cardElevation(2.dp),
            content = {
                onDeleteColor?.let {delete->
                    FilledIconButton(
                        onClick = { delete.invoke(color) },
                        modifier = Modifier
                            .padding(top = 6.dp, end = 6.dp)
                            .size(24.dp)
                            .align(Alignment.End),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = colorSelect(),
                            contentColor = colorSelect(saturation = 90,inverse = true)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_24),
                            contentDescription = "Delete"
                        )
                    }
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.8f)) {
                Text(
                    text = color.name,
                    color = colorSelect(inverse = true),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(text = color.value.color().toHexString(), color = colorSelect(inverse = true))
            }
            Box(modifier = Modifier.weight(0.2f)){
                IconButton(onClick = {
                    GlobalSignals.snackbarHostState.tryEmit("Copied: ${color.value.color().toHexString()}")
                    clipboardManager.setText(
                        AnnotatedString(buildString {
                            append(color.value.color().toHexString())
                        })
                    )
                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "CopyIcon",
                        modifier = Modifier.size(32.dp),
                        tint = colorSelect(inverse = true)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDcpColorCard() {
    DcpColorCard(
        color = ColorInfo(
            value = Color(0xFFF44336).string(),
            name = "500",
            palette = Palette.Material(subPalette = ColorOfMaterial.RED)
        ),
        onColorClick = {},
        onDeleteColor = {}
    )
}