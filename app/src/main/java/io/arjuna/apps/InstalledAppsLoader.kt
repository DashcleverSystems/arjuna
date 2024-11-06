package io.arjuna.apps

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import io.arjuna.logging.ARJUNA_TAG

class InstalledAppsLoader(
    private val packageManager: PackageManager
) {

    fun getAll(): Set<InstalledApp> {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter {
                (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 ||
                        (it.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
            }
            .mapNotNull { it.loadLabel(this.packageManager) }
            .map { InstalledApp(it.toString()) }.toSet()
    }

    fun getIcon(app: InstalledApp): Drawable? {
        Log.d(ARJUNA_TAG, "Loading icon for $app")
        try {
            return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                .firstOrNull { it.loadLabel(this.packageManager) == app.name }
                ?.let { packageManager.getApplicationIcon(it) }
                .also { Log.d(ARJUNA_TAG, "Finished loading icon for ${app.name}") }
        } catch (ex: Exception) {
            Log.w(ARJUNA_TAG, "Error loading $app icon", ex)
            return null
        }
    }
}