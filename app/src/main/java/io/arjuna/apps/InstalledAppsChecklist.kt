package io.arjuna.apps

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

@Composable
fun InstalledAppsChecklist(
    state: InstalledAppsChecklistState,
    appIconLoader: (InstalledApp) -> Drawable? = { _ -> null },
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.installedApps.forEach { app ->
            val isChecked = state.selectedApps.any { it == app }
            Row(
                Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    isChecked,
                    onCheckedChange = {
                        if (it) state.selectedApps += app else state.selectedApps -= app
                    })
                val appIconImageBitmap = appIconLoader.invoke(app)?.toBitmap()
                appIconImageBitmap?.let { Image(it.asImageBitmap(), "${app.name} icon") }
                Text(app.name)
            }
        }
    }
}

class InstalledAppsChecklistState(
    val installedApps: Set<InstalledApp> = emptySet(),
    initialSelectedAppNames: Set<InstalledApp> = emptySet()
) {
    var selectedApps by mutableStateOf(initialSelectedAppNames)
}

