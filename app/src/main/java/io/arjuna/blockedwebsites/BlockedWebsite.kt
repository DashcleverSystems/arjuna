package io.arjuna.blockedwebsites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.arjuna.composables.MainElevatedCardModifier
import io.arjuna.composables.OutlinedCard
import io.arjuna.composables.OutlinedCards

@Composable
fun BlockedWebsites(
    blockedWebsites: Set<BlockedWebsite>,
    onWebsiteRemove: (BlockedWebsite) -> Unit = {},
    addButtonProvider: @Composable () -> Unit = {}
) {
    ElevatedCard(MainElevatedCardModifier.wrapContentWidth(Alignment.CenterHorizontally)) {
        Text("Websites", Modifier.padding(4.dp), fontWeight = FontWeight.Bold)
        OutlinedCards(
            blockedWebsites,
            onEmptyContent = {
                OutlinedCard {
                    WebsiteText("Nothing to protect from yet!")
                }
            },
            elementComposable = { Website(it, onWebsiteRemove) },
            additionalContent = addButtonProvider
        )
    }
}

@Composable
private fun Website(
    website: BlockedWebsite,
    onRemove: (BlockedWebsite) -> Unit
) {
    OutlinedCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            WebsiteText(website.mainDomain)
            RemoveButton { onRemove.invoke(website) }
        }
    }
}

@Composable
private fun WebsiteText(domain: String) {
    Text(
        domain,
        Modifier
            .padding(6.dp)
            .fillMaxWidth(0.7f)
    )
}

@Composable
private fun RemoveButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .size(25.dp)
            .padding(6.dp),
        onClick = { onClick() },
    ) {
        Icon(Icons.Sharp.Clear, "Remove website")
    }
}
