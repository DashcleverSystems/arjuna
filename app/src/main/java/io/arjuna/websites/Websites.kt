package io.arjuna.websites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import io.arjuna.composables.CommonElevatedCardModifier
import io.arjuna.composables.OutlinedCard
import io.arjuna.composables.OutlinedCards

@Composable
fun Websites(
    modifier: Modifier = CommonElevatedCardModifier,
    websites: Set<Website>,
    onWebsiteRemove: (Website) -> Unit = {},
    addButtonProvider: @Composable () -> Unit = {},
) {
    ElevatedCard(modifier) {
        Text(
            "Websites",
            Modifier
                .padding(4.dp),
            fontWeight = FontWeight.Bold
        )
        OutlinedCards(
            websites,
            onEmptyContent = {
                OutlinedCard(Modifier.align(Alignment.CenterHorizontally)) {
                    WebsiteText("Nothing to protect from yet!")
                }
            },
            elementComposable = {
                Website(
                    Modifier.align(Alignment.CenterHorizontally),
                    it,
                    onWebsiteRemove
                )
            },
            additionalContent = addButtonProvider
        )
    }
}

@Composable
private fun Website(
    modifier: Modifier,
    website: Website,
    onRemove: (Website) -> Unit
) {
    OutlinedCard(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
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
