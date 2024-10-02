package io.arjuna.blockedwebsites

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import io.arjuna.proto.BlockedWebsite as BlockedWebsiteProto
import io.arjuna.proto.BlockedWebsites as BlockedWebsitesProto

data class BlockedWebsite(
    val identifier: Id = Id(),
    val mainDomain: String
) {
    data class Id(val uuid: UUID = UUID.randomUUID())
}

class BlockedWebsiteRepository(
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore<BlockedWebsitesProto>
) {
    val blockedWebsites: Flow<Set<BlockedWebsite>> =
        dataStore.data.map { blockedWebsites ->
            blockedWebsites.websitesList.map {
                BlockedWebsite(
                    BlockedWebsite.Id(it.identifier.toUUID()),
                    it.domain
                )
            }
                .toSet()
        }

    fun addWebsiteToBlock(website: BlockedWebsite) {
        coroutineScope.launch {
            dataStore.updateData { currentData: BlockedWebsitesProto ->
                if (currentData.contains(website)) return@updateData currentData
                val newDataBuilder = BlockedWebsitesProto.newBuilder()
                newDataBuilder.addAllWebsites(currentData.websitesList + website.toProto())
                return@updateData newDataBuilder.build()
            }
        }
    }

    fun removeWebsiteToBlock(website: BlockedWebsite) {
        coroutineScope.launch {
            dataStore.updateData { currentData: BlockedWebsitesProto ->
                val websiteToRemove =
                    currentData.websitesList.firstOrNull { it: BlockedWebsiteProto ->
                        it.identifier.toUUID() == website.identifier.uuid
                    } ?: return@updateData currentData
                val newDataBuilder = BlockedWebsitesProto.newBuilder()
                newDataBuilder.addAllWebsites(currentData.websitesList - websiteToRemove)
                return@updateData newDataBuilder.build()
            }
        }
    }
}
