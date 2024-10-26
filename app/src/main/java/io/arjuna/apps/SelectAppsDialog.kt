package io.arjuna.apps

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SelectAppsDialog(
    state: InstalledAppsChecklistState,
    appIconLoader: (InstalledApp) -> Drawable? = { _ -> null },
    onClose: (Set<InstalledApp>) -> Unit
) {
    Dialog(onDismissRequest = { onClose(emptySet()) }) {
        Card {
            Text(
                modifier = Modifier.padding(6.dp),
                text = "Add apps to block",
                fontWeight = FontWeight.Bold
            )
            Column {
                Row(
                    Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.8f)
                ) {
                    InstalledAppsChecklist(state, appIconLoader)
                }
                Row(Modifier.fillMaxWidth(0.8f)) {
                    Button({ onClose(state.selectedApps) }, Modifier.fillMaxWidth()) {
                        Icon(Icons.Rounded.Check, "Select apps")
                    }
                }
            }
        }
    }
}