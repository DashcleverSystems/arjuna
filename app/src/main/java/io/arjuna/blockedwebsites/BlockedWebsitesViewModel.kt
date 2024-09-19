package io.arjuna.blockedwebsites

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class BlockedWebsitesViewModel(
    private val blockedWebsitesRepository: BlockedWebsiteRepository
) : ViewModel() {

    val blockedWebsites: Flow<Set<BlockedWebsite>> =
        blockedWebsitesRepository.blockedWebsites

    fun addWebsiteToBlock(website: BlockedWebsite) {
        blockedWebsitesRepository.addWebsiteToBlock(website)
    }

    fun removeWebsiteToBlock(website: BlockedWebsite) =
        blockedWebsitesRepository.removeWebsiteToBlock(website)
}
