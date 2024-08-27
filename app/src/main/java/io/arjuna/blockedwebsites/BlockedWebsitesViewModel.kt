package io.arjuna.blockedwebsites

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import io.arjuna.proto.BlockedWebsites as BlockedWebsitesProto

class BlockedWebsitesViewModel(private val blockedWebsitesStore: DataStore<BlockedWebsitesProto>) :
    ViewModel() {

    data class BlockedWebsite(val mainDomain: String)

    val blockedWebsites: Flow<Set<BlockedWebsite>> =
        blockedWebsitesStore.data.map { blockedWebsites ->
            blockedWebsites.websitesList.map { BlockedWebsite(it.domain) }.toSet()
        }

    fun addWebsiteToBlock(website: BlockedWebsite) {
        viewModelScope.launch {
            blockedWebsitesStore.updateData { currentData: BlockedWebsitesProto ->
                if (currentData.contains(website)) return@updateData currentData
                val websiteToBlock =
                    io.arjuna.proto.BlockedWebsite.newBuilder()
                        .setDomain(website.mainDomain)
                        .build()
                val newDataBuilder = BlockedWebsitesProto.newBuilder()
                newDataBuilder.addAllWebsites(currentData.websitesList + websiteToBlock)
                return@updateData newDataBuilder.build()
            }
        }
    }

    private fun BlockedWebsitesProto.contains(website: BlockedWebsite): Boolean =
        this.websitesList.any { it.domain == website.mainDomain }
}