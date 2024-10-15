package io.arjuna.websites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.arjuna.composables.ElevatedCardTitle

@Composable
fun Websites(
    modifier: Modifier = Modifier,
    websites: Set<Website>,
    onWebsiteRemove: ((Website) -> Unit)? = null,
    onWebsiteAdd: ((Website) -> Unit)? = null
) {
    Column {
        ElevatedCard(modifier) {
            ElevatedCardTitle("Websites")
            WebsitesListing(Modifier.align(Alignment.CenterHorizontally), websites, onWebsiteRemove)
            var showAddDialog by remember { mutableStateOf(false) }

            if (onWebsiteAdd == null) return@ElevatedCard

            AddDialog(showAddDialog) { domain: String? ->
                showAddDialog = false
                if (domain != null) {
                    val website = Website(mainDomain = domain); onWebsiteAdd.invoke(website)
                }
            }
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
    }
}

@Composable
private fun AddDialog(
    showDialog: Boolean,
    onClose: (String?) -> Unit
) {
    if (showDialog.not()) return

    Dialog(onDismissRequest = { onClose(null) }) {
        Card {
            Text(
                modifier = Modifier.padding(6.dp),
                text = "Add website to block",
                fontWeight = FontWeight.Bold
            )
            Column {
                var website by remember { mutableStateOf("") }
                TextField(
                    label = { Text("Website") },
                    modifier = Modifier.padding(4.dp),
                    value = website,
                    onValueChange = { website = it })
                Button(
                    shape = RoundedCornerShape(100),
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .padding(end = 4.dp),
                    onClick = { onClose(website) }) {
                    Icon(Icons.Sharp.AddCircle, "Add")
                }
            }
        }
    }
}






