package io.arjuna

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.arjuna.blockedwebsites.AddDialogComposable
import io.arjuna.blockedwebsites.BlockedWebsite
import io.arjuna.blockedwebsites.BlockedWebsites

@Composable
fun HomeComposable(
    blockedWebsites: Set<BlockedWebsite>,
    onWebsiteRemove: (BlockedWebsite) -> Unit,
    onWebsiteAdd: (String?) -> Unit,
    schedulesOverviewProvider: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BlockedWebsites(blockedWebsites, { onWebsiteRemove(it) }) {
            var showAddDialog by remember { mutableStateOf(false) }
            AddDialogComposable(showAddDialog) { showAddDialog = false; onWebsiteAdd(it) }

            SmallFloatingActionButton(
                shape = RoundedCornerShape(100),
                modifier = Modifier
                    .padding(end = 10.dp, bottom = 10.dp)
                    .align(Alignment.End),
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Sharp.Add, "Adds website to block")
            }
        }
        schedulesOverviewProvider()
    }
}