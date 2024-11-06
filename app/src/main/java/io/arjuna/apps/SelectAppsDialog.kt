package io.arjuna.apps

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Arrangement
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
    state: List<InstalledAppsChecklistElement>,
    appIconLoader: (InstalledApp) -> Drawable?,
    onClose: (List<InstalledAppsChecklistElement>) -> Unit
) {
    Dialog(onDismissRequest = { onClose(emptyList()) }) {
        Card(Modifier.fillMaxWidth(0.8f)) {
            Text(
                modifier = Modifier.padding(15.dp),
                text = "Add apps to block",
                fontWeight = FontWeight.Bold
            )
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                ) {
                    InstalledAppsChecklist(state, appIconLoader)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        { onClose(state) },
                        Modifier.fillMaxWidth().padding(6.dp)
                    ) {
                        Icon(Icons.Rounded.Check, "Select apps")
                    }
                }
            }
        }
    }
}