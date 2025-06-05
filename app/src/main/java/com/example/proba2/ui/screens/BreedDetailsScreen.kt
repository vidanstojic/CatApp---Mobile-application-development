package com.example.proba2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proba2.ui.compose.BreedAppTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen(
    breedId: String,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            BreedAppTopBar(
                text = "Password Details",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                navigationOnClick = onClose,
                actionIcon = Icons.Default.Edit,
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 32.dp)
        ) {


            Text(
                text = "Title: Naslov za $breedId"
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Text(
                text = "Password: Lozinka za $breedId"
            )
        }
    }

}