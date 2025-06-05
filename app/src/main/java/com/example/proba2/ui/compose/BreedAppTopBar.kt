package com.example.proba2.ui.compose

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


@ExperimentalMaterial3Api
@Composable
fun BreedAppTopBar(
    modifier: Modifier = Modifier,
    text: String,
    navigationIcon: ImageVector? = null,
    navigationOnClick: (() -> Unit)? = null,
    actionIcon: ImageVector? = null,
    actionOnClick: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
        navigationIcon = {
            if (navigationIcon != null) {
                AppTopBarIcon(
                    icon = navigationIcon,
                    onClick = { navigationOnClick?.invoke() },
                )
            }
        },
        title = {
            Text(text = text)
        },
        actions = {
            if (actionIcon != null) {
                AppTopBarIcon(
                    icon = actionIcon,
                    onClick = { actionOnClick?.invoke() }
                )
            }
        },
    )
}