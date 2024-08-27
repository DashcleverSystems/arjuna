package io.arjuna.blockedwebsites

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp

@Composable
fun BlockedWebsitesComposable(
    blockedWebsites: Set<BlockedWebsitesViewModel.BlockedWebsite>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(Dp(5F)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        blockedWebsites.forEach {
            Text(
                text = AnnotatedString(it.mainDomain),
                modifier = Modifier.padding(horizontal = Dp(8F)),
            )
        }
    }
}