package com.honyadew.saved.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honyadew.designsystem.view.DcpAlertDialog
import com.honyadew.designsystem.view.DcpColorCard
import com.honyadew.saved.contact.SavedState
import com.honyadew.saved.model.SavedTabs
import com.honyadew.saved.view.part.SavedColorSchemeCard
import com.honyadew.designsystem.theme.colorSelect
import com.honyadew.designsystem.view.MiniColorCard
import com.honyadew.extencion.string
import com.honyadew.model.ColorInfo
import com.honyadew.model.CustomColorScheme
import kotlinx.coroutines.launch

@Composable
fun SavedViewShow(
    state: SavedState.Show,
    onDeleteClick: (colorScheme: com.honyadew.model.CustomColorScheme) -> Unit,
    onOpenColorScheme: (colorScheme: com.honyadew.model.CustomColorScheme) -> Unit,
    onDismissOpen: () -> Unit,
    onChangeTab: (newTab: SavedTabs) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()){
        state.openedColorScheme?.let { colorScheme ->
            //TODO, add a way to edit name (for example pass here nullable function)
            SavedDialog(colorScheme = colorScheme, onDismiss = onDismissOpen)
        }
        Column {
            SavedTabRow(
                selectedTab = state.selectedTab,
                onChangeTab = onChangeTab
            )
            SavedPager(
                colorSchemes = state.colorsToShow,
                onDeleteClick = onDeleteClick,
                onOpenColorScheme = onOpenColorScheme
            )
        }
    }
}

//TODO MAKE THIS REAL HORIZONTAL_PAGER, USING rememberPagerState and others
@Composable
private fun SavedPager(
    colorSchemes : List<com.honyadew.model.CustomColorScheme>,
    onDeleteClick: (colorScheme: com.honyadew.model.CustomColorScheme) -> Unit,
    onOpenColorScheme: (colorScheme: com.honyadew.model.CustomColorScheme) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box( modifier = modifier) {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 512.dp)){
            colorSchemes.forEach {colorScheme ->
                item {
                    if (colorScheme.colors.size == 1) {
                        DcpColorCard(
                            color = com.honyadew.model.ColorInfo(
                                value = colorScheme.colors[0].value,
                                name = colorScheme.name
                            ),
                            onColorClick = {onOpenColorScheme.invoke(colorScheme)},
                            onDeleteColor = {onDeleteClick.invoke(colorScheme)},
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        SavedColorSchemeCard(
                            paletteInfo = colorScheme,
                            onPaletteClick = onOpenColorScheme,
                            onDeletePalette = onDeleteClick,
                            modifier = Modifier.padding(8.dp)

                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedDialog(
    colorScheme: com.honyadew.model.CustomColorScheme,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DcpAlertDialog(
        title = colorScheme.name,
        onDismiss = onDismiss,
    ) {
        LazyVerticalGrid(columns = GridCells.Adaptive(minOf(96.dp))){
            colorScheme.colors.forEach { colorInfo ->
                item {
                    MiniColorCard(color = colorInfo, modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@Composable
private fun SavedTabRow(
    selectedTab: SavedTabs,
    onChangeTab: (newTab: SavedTabs) -> Unit,
    modifier: Modifier = Modifier
){
    TabRow(
        modifier = modifier,
        selectedTabIndex = SavedTabs.values().indexOf(selectedTab),
        containerColor = colorSelect(),
        contentColor = colorSelect(saturation = 90, inverse = true),
        indicator = {tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(
                        tabPositions[SavedTabs
                            .values()
                            .indexOf(selectedTab)]
                    )
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(colorSelect(saturation = 90, inverse = true))
            )

        }
    ) {
        SavedTabs.values().forEach{ tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = {onChangeTab.invoke(tab)},
                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
            ) {
                val iconRes = if (selectedTab == tab){
                    tab.selectedTabId
                } else {
                    tab.unselectedTabId
                }
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = stringResource(id = tab.tabNameId)
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewSavedViewShow(){
    SavedViewShow(
        state = SavedState.Show(
            colorsToShow = listOf(
                CustomColorScheme(
                    colors = listOf(
                        ColorInfo(
                            value = Color.Green.string(),
                            name = "test color"
                        )
                    ),
                    name = "test color scheme"
                ),
                CustomColorScheme(
                    colors = listOf(
                        ColorInfo(
                            value = Color.Green.string(),
                            name = "test color"
                        ),
                        ColorInfo(
                            value = Color.Yellow.string(),
                            name = "test color"
                        ),
                        ColorInfo(
                            value = Color.Green.string(),
                            name = "test color"
                        ),
                    ),
                    name = "test color scheme2"
                ),
                CustomColorScheme(
                    colors = listOf(
                        ColorInfo(
                            value = Color.Green.string(),
                            name = "test color"
                        ),
                        ColorInfo(
                            value = Color.Yellow.string(),
                            name = "test color"
                        ),
                        ColorInfo(
                            value = Color.Green.string(),
                            name = "test color"
                        ),
                        ColorInfo(
                            value = Color.Yellow.string(),
                            name = "test color"
                        ),
                        ColorInfo(
                            value = Color.Green.string(),
                            name = "test color"
                        ),
                    ),
                    name = "test color scheme3"
                )
            ),
            selectedTab = SavedTabs.ONE_COLOR,
            openedColorScheme = CustomColorScheme(
                colors = listOf(
                    ColorInfo(value = Color.Green.string(), name = "test color"),
                    ColorInfo(
                        value = Color.Yellow.string(),
                        name = "test color"
                    ),
                    ColorInfo(value = Color.Green.string(), name = "test color"),
                    ColorInfo(
                        value = Color.Yellow.string(),
                        name = "test color"
                    ),
                    ColorInfo(value = Color.Green.string(), name = "test color"),
                ),
                name = "test color scheme3"
            )
        ),
        onDeleteClick = {},
        onOpenColorScheme = {},
        onDismissOpen = { /*TODO*/ },
        onChangeTab = {}
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun TestPager(){
    val items = listOf("Item 1", "Item 2", "Item 3")

    val coroutine = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0)

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            items.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutine.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            pageCount = items.size,
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            Text(
                text = items[page],
                fontSize = 24.sp,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}