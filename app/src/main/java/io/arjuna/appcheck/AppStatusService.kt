package io.arjuna.appcheck

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.text.TextUtils

class AppStatusService(
    private val context: Context,
    private val websitesBlockingAccessibilityServiceProvider: () -> Class<*>
) {

    fun isAllowedToBlockWebsites(): Boolean {
        val appAccessibilityServiceClass = websitesBlockingAccessibilityServiceProvider.invoke()
        val expectedComponentName = ComponentName(context, appAccessibilityServiceClass)
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServices)

        while (colonSplitter.hasNext()) {
            val componentName = colonSplitter.next()
            if (ComponentName.unflattenFromString(componentName) == expectedComponentName) {
                return true
            }
        }

        return false
    }
}