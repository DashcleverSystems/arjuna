package io.arjuna.blockedwebsites

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import io.arjuna.logging.ARJUNA_TAG

class UrlInterceptorService(

    private val onBlockedWebsite: () -> Unit = {}
) : AccessibilityService() {

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

        val eventTime = event.eventTime
        Log.d(ARJUNA_TAG, "Detected website to block $capturedUrl on $eventTime")
        onBlockedWebsite.invoke()
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