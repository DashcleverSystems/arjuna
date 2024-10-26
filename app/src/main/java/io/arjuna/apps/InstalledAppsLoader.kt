package io.arjuna.apps

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

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
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .firstOrNull { it.loadLabel(this.packageManager) == app.name }
            ?.let { packageManager.getApplicationIcon(it) }
    }
}