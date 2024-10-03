package io.arjuna.websites

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class WebsitesViewModel(
    private val blockedWebsitesRepository: WebsiteRepository
) : ViewModel() {

    val websites: Flow<Set<Website>> =
        blockedWebsitesRepository.websites

    fun addWebsiteToBlock(website: Website) {
        blockedWebsitesRepository.addWebsiteToBlock(website)
    }

    fun removeWebsiteToBlock(website: Website) =
        blockedWebsitesRepository.removeWebsiteToBlock(website)
}
