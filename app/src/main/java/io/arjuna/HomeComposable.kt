package io.arjuna

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.arjuna.blockedwebsites.BlockedWebsitesComposable
import io.arjuna.blockedwebsites.BlockedWebsitesViewModel

@Composable
fun HomeComposable(
    blockedWebsites: Set<BlockedWebsitesViewModel.BlockedWebsite>,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dp(30F)),
    ) { innerPadding: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { BlockedWebsitesComposable(blockedWebsites) }
    }
}