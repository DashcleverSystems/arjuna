package io.arjuna

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.arjuna.websites.AddDialog
import io.arjuna.websites.Website
import io.arjuna.websites.Websites

@Composable
fun HomeComposable(
    elementsModifier: Modifier,
    isAppAllowedToBlock: Boolean,
    websites: Set<Website>,
    onWebsiteRemove: (Website) -> Unit,
    onWebsiteAdd: (String?) -> Unit,
    schedulesOverviewProvider: @Composable (Modifier) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isAppAllowedToBlock.not()) {
            ElevatedCard(
                elementsModifier.align(Alignment.CenterHorizontally),
                colors = CardDefaults.elevatedCardColors().copy(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "App will not block any of your activity!",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                )
                Text(
                    "Please go to accessibility settings and allow Arjuna",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                )
            }
        }
        Websites(
            elementsModifier.align(Alignment.CenterHorizontally),
            websites = websites,
            onWebsiteRemove = { onWebsiteRemove(it) }) {
            var showAddDialog by remember { mutableStateOf(false) }
            AddDialog(showAddDialog) { showAddDialog = false; onWebsiteAdd(it) }

            SmallFloatingActionButton(
                shape = RoundedCornerShape(100),
                modifier = Modifier
                    .padding(end = 20.dp, bottom = 10.dp)
                    .align(Alignment.End),
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Sharp.Add, "Adds website to block")
            }
        }
        schedulesOverviewProvider(elementsModifier.align(Alignment.CenterHorizontally))
    }
}