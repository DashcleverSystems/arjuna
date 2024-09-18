package io.arjuna.blockedwebsites

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BlockedWebsitesViewModel(
    private val blockedWebsitesRepository: BlockedWebsiteRepository
) : ViewModel() {

    data class BlockedWebsite(val mainDomain: String)

    val blockedWebsites: Flow<Set<BlockedWebsite>> =
        blockedWebsitesRepository.blockedWebsites
            .map { blockedWebsites: Set<io.arjuna.blockedwebsites.BlockedWebsite> ->
                blockedWebsites.map { BlockedWebsite(it.mainDomain) }.toSet()
            }

    fun addWebsiteToBlock(website: BlockedWebsite) {
        blockedWebsitesRepository.addWebsiteToBlock(
            io.arjuna.blockedwebsites.BlockedWebsite(website.mainDomain)
        )
    }

    fun removeWebsiteToBlock(website: BlockedWebsite) {
        blockedWebsitesRepository.removeWebsiteToBlock(
            io.arjuna.blockedwebsites.BlockedWebsite(website.mainDomain)
        )
    }
}
