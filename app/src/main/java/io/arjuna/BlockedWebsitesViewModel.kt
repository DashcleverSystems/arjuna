package io.arjuna

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.arjuna.proto.BlockedWebsites
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BlockedWebsitesViewModel(private val blockedWebsitesStore: DataStore<BlockedWebsites>) :
    ViewModel() {

    data class BlockedWebsite(val mainDomain: String)

    val blockedWebsites: Flow<Set<BlockedWebsite>> =
        blockedWebsitesStore.data.map { blockedWebsites ->
            blockedWebsites.websitesList.map { BlockedWebsite(it.domain) }.toSet()
        }

    fun addWebsiteToBlock(website: BlockedWebsite) {
        viewModelScope.launch {
            blockedWebsitesStore.updateData { currentData: BlockedWebsites ->
                val websiteToBlock =
                    io.arjuna.proto.BlockedWebsite.newBuilder()
                        .setDomain("test click")
                        .build()
                val newDataBuilder = BlockedWebsites.newBuilder()
                newDataBuilder.addAllWebsites(currentData.websitesList + websiteToBlock)
                newDataBuilder.build()
            }
        }
    }
}