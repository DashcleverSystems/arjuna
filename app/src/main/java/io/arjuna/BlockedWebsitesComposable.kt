package io.arjuna

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString

@Composable
fun BlockedWebsites(
    modifier: Modifier,
    blockedWebsites: Set<BlockedWebsitesViewModel.BlockedWebsite>
) {
    Card(
        modifier = modifier.fillMaxWidth(0.7f)
    ) {
        blockedWebsites.forEach {
            Text(AnnotatedString(it.mainDomain))
        }
    }
}