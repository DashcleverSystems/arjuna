package io.arjuna.blockedwebsites

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import io.arjuna.proto.BlockedWebsite as BlockedWebsiteProto
import io.arjuna.proto.BlockedWebsites as BlockedWebsitesProto
import io.arjuna.proto.UUID as UUIDProto

data class BlockedWebsite(
    val identifier: UUID = UUID.randomUUID(),
    val mainDomain: String
)

class BlockedWebsiteRepository(
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore<BlockedWebsitesProto>
) {
    val blockedWebsites: Flow<Set<BlockedWebsite>> =
        dataStore.data.map { blockedWebsites ->
            blockedWebsites.websitesList.map { BlockedWebsite(it.identifier.toUUID(), it.domain) }
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
                        it.identifier.toUUID() == website.identifier
                    } ?: return@updateData currentData
                val newDataBuilder = BlockedWebsitesProto.newBuilder()
                newDataBuilder.addAllWebsites(currentData.websitesList - websiteToRemove)
                return@updateData newDataBuilder.build()
            }
        }
    }

    private fun BlockedWebsitesProto.contains(website: BlockedWebsite): Boolean =
        this.websitesList.any { it.domain == website.mainDomain }

    private fun UUID.toProto(): UUIDProto = UUIDProto.newBuilder()
        .setValue(this.toString())
        .build()

    private fun UUIDProto.toUUID(): UUID = UUID.fromString(this.value)

    private fun BlockedWebsite.toProto(): BlockedWebsiteProto =
        BlockedWebsiteProto.newBuilder().apply {
            this.domain = this@toProto.mainDomain
            this.identifier = this@toProto.identifier.toProto()
        }.build()
}
