package com.example.proba2.ui.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
@Composable
fun AppTopBar(
    logoPainter: Painter,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp) // 👈 POVECANA VISINA TOP BARA
            .background(MaterialTheme.colorScheme.primary) // Možeš prilagoditi boju
            .padding(horizontal = 10.dp)
            .padding(vertical = 15.dp)
            .shadow(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(), // koristi fillMaxSize da zauzme pun prostor
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.size(32.dp), // 👈 POVECANA IKONICA
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    painter = logoPainter,
                    contentDescription = "Catalist logo",
                    modifier = Modifier.size(32.dp), // 👈 POVECANA VELIČINA LOGO IKONE
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Catalist",
                    style = MaterialTheme.typography.headlineSmall, // 👈 VEĆI TEKST
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(32.dp), // 👈 POVECANA IKONICA
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

