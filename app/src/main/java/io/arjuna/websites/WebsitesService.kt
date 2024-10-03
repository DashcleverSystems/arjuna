package io.arjuna.websites

import android.util.Log
import io.arjuna.logging.ARJUNA_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class WebsitesService(
    private val websiteRepository: WebsiteRepository,
    private val coroutineScope: CoroutineScope
) {

    fun onUrlChange(
        urlChanged: UrlChanged,
        onBlockedWebsite: () -> Unit
    ) {
        coroutineScope.launch {
            websiteRepository.websites.collect { blockedWebsites ->
                if (blockedWebsites.isEmpty()) {
                    Log.d(ARJUNA_TAG, "Blocked websites is empty. Not blocking $urlChanged")
                    return@collect
                }
                if (blockedWebsites.blocks(urlChanged.url)) {
                    Log.d(ARJUNA_TAG, "Blocking: ${urlChanged.url}")
                    onBlockedWebsite.invoke()
                }
            }
        }
    }

    private fun Collection<Website>.blocks(url: String) =
        this.any { url.contains(it.mainDomain, ignoreCase = true) }

    data class UrlChanged(val url: String)
}