package io.arjuna

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComposable(navActions: NavActions) {
    TopAppBar(
        title = { Text("Arjuna") },
        navigationIcon = {
            IconButton(onClick = navActions::navigateToHome) {
                Icon(Icons.Rounded.Home, "Home")
            }
        }
    )
}