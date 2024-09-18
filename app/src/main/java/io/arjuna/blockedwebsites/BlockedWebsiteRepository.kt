package io.arjuna.blockedwebsites

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import io.arjuna.proto.BlockedWebsites as BlockedWebsitesProto

class BlockedWebsiteRepository(
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore<BlockedWebsitesProto>
) {
    val blockedWebsites: Flow<Set<BlockedWebsite>> =
        dataStore.data.map { blockedWebsites ->
            blockedWebsites.websitesList.map { BlockedWebsite(it.domain) }.toSet()
        }

    fun addWebsiteToBlock(website: BlockedWebsite) {
        coroutineScope.launch {
            dataStore.updateData { currentData: BlockedWebsitesProto ->
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

    fun removeWebsiteToBlock(website: BlockedWebsite) {
        coroutineScope.launch {
            dataStore.updateData { currentData: BlockedWebsitesProto ->
                val websiteToRemove =
                    currentData.websitesList.firstOrNull { it.domain == website.mainDomain }
                        ?: return@updateData currentData
                val newDataBuilder = BlockedWebsitesProto.newBuilder()
                newDataBuilder.addAllWebsites(currentData.websitesList - websiteToRemove)
                return@updateData newDataBuilder.build()
            }
        }
    }


    private fun BlockedWebsitesProto.contains(website: BlockedWebsite): Boolean =
        this.websitesList.any { it.domain == website.mainDomain }
}

data class BlockedWebsite(
    val mainDomain: String
)