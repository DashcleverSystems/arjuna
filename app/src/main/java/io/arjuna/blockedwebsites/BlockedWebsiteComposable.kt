package io.arjuna.blockedwebsites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BlockedWebsitesComposable(
    blockedWebsites: Set<BlockedWebsitesViewModel.BlockedWebsite>,
    onWebsiteRemove: (BlockedWebsitesViewModel.BlockedWebsite) -> Unit = {},
    addButtonProvider: @Composable () -> Unit = {}
) {
    ElevatedCard(
        Modifier
            .padding(4.dp)
            .fillMaxWidth(0.9f)
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        Text(
            "Blocked websites",
            Modifier.padding(start = 10.dp, top = 10.dp),
            fontWeight = FontWeight.Bold
        )
        blockedWebsites.ifEmpty {
            BlockedWebsiteCard {
                BlockedWebsiteText("Nothing to protect from yet!")
            }
        }
        blockedWebsites.forEach { blockedWebsite ->
            BlockedWebsiteCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BlockedWebsiteText(blockedWebsite.mainDomain)
                    RemoveButton { onWebsiteRemove(blockedWebsite) }
                }
            }
        }
        addButtonProvider()
    }
}

@Composable
private fun BlockedWebsiteCard(content: @Composable ColumnScope.() -> Unit) {
    OutlinedCard(
        Modifier
            .padding(10.dp)
            .fillMaxWidth(0.7f)
    ) {
        content()
    }
}

@Composable
private fun BlockedWebsiteText(domain: String) {
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
