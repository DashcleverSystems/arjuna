package io.arjuna.websites

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import io.arjuna.proto.Website as WebsiteProto
import io.arjuna.proto.Websites as WebsitesProto

data class Website(
    val identifier: Id = Id(),
    val mainDomain: String
) {
    data class Id(val uuid: UUID = UUID.randomUUID())
}

class WebsiteRepository(
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore<WebsitesProto>
) {
    val websites: Flow<Set<Website>> =
        dataStore.data.map { blockedWebsites ->
            blockedWebsites.websitesList.map {
                Website(
                    Website.Id(it.identifier.toUUID()),
                    it.domain
                )
            }
                .toSet()
        }

    fun addWebsiteToBlock(website: Website) {
        coroutineScope.launch {
            dataStore.updateData { currentData: WebsitesProto ->
                if (currentData.contains(website)) return@updateData currentData
                val newDataBuilder = WebsitesProto.newBuilder()
                newDataBuilder.addAllWebsites(currentData.websitesList + website.toProto())
                return@updateData newDataBuilder.build()
            }
        }
    }

    fun removeWebsiteToBlock(website: Website) {
        coroutineScope.launch {
            dataStore.updateData { currentData: WebsitesProto ->
                val websiteToRemove =
                    currentData.websitesList.firstOrNull { it: WebsiteProto ->
                        it.identifier.toUUID() == website.identifier.uuid
                    } ?: return@updateData currentData
                val newDataBuilder = WebsitesProto.newBuilder()
                newDataBuilder.addAllWebsites(currentData.websitesList - websiteToRemove)
                return@updateData newDataBuilder.build()
            }
        }
    }
}
