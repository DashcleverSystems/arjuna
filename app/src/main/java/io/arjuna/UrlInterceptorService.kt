package io.arjuna

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import io.arjuna.logging.ARJUNA_TAG
import io.arjuna.schedule.infra.proto.ScheduleRepository
import io.arjuna.schedule.infra.proto.schedulesStore
import io.arjuna.websites.WebsitesService
import io.arjuna.websites.WebsitesService.UrlChanged

class UrlInterceptorService : AccessibilityService() {

    private val websitesService by lazy {
        val repository = ScheduleRepository(
            DEFAULT_COROUTINE_SCOPE,
            baseContext.schedulesStore
        )
        WebsitesService(
            repository,
            DEFAULT_COROUTINE_SCOPE
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

        // i have just realise it doesn't make sense to open another google tab.
        // Reasoning was to not redirect user again to warn screen once chrome is opened
        // again by user and the recent app, facebook is opened.
        websitesService.onUrlChange(UrlChanged(capturedUrl)) {
            val nextChromeTab =
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://keep.google.com")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    setPackage(packageName)
                }
            val arjunaWarnScreen = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("warn", true)
                putExtra("url", capturedUrl)
            }
            startActivity(nextChromeTab)
            startActivity(arjunaWarnScreen)
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
