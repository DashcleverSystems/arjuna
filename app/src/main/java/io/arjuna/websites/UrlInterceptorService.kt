package io.arjuna.websites

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import io.arjuna.MainActivity
import io.arjuna.websites.WebsitesService.UrlChanged
import io.arjuna.logging.ARJUNA_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class UrlInterceptorService : AccessibilityService() {

    private val websitesService by lazy {
        val repository = WebsiteRepository(
            CoroutineScope(Dispatchers.Main + SupervisorJob()),
            baseContext.blockedWebsitesStore
        )
        WebsitesService(
            repository,
            CoroutineScope(Dispatchers.Main + SupervisorJob())
        )
    }

    private val supportedBrowsers = InMemorySupportedBrowserProvider.supportedBrowser

    override fun onServiceConnected() {
        this.serviceInfo = serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            packageNames = supportedBrowsers.map { it.packageName }.toTypedArray()
            feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
            notificationTimeout = 300
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val parentNodeInfo = event.source
            ?: return

        val packageName = event.packageName.toString()
        val browser = supportedBrowsers.firstOrNull { it.packageName == packageName }
            ?: return

        val capturedUrl = captureUrl(parentNodeInfo, browser)
            ?: return
        Log.d(ARJUNA_TAG, "Captured url: $capturedUrl")

        websitesService.onUrlChange(UrlChanged(capturedUrl)) {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }

    private fun captureUrl(
        nodeInfo: AccessibilityNodeInfo,
        config: SupportedBrowser
    ): String? {
        val nodes = nodeInfo.findAccessibilityNodeInfosByViewId(config.addressBarId)
            .takeIf { it.size > 0 }
            ?: return null

        val addressBarNodeInfo = nodes[0]
        return addressBarNodeInfo?.text?.toString()
    }

    override fun onInterrupt() {}
}

data class SupportedBrowser(
    val packageName: String,
    val addressBarId: String
)

interface SupportedBrowserProvider {
    val supportedBrowser: Set<SupportedBrowser>
}

object InMemorySupportedBrowserProvider : SupportedBrowserProvider {
    override val supportedBrowser: Set<SupportedBrowser> = setOf(
        SupportedBrowser("com.android.chrome", "com.android.chrome:id/url_bar"),
        SupportedBrowser("org.mozilla.firefox", "org.mozilla.firefox:id/url_bar_title")
    )

}
