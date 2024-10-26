package io.arjuna.apps

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class InstalledAppsLoader(
    private val packageManager: PackageManager
) {

    fun getAll(): Set<InstalledApp> {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .mapNotNull { it.packageName.split(".").last() }
            .map { InstalledApp(it) }.toSet()
    }

    fun getIcon(app: InstalledApp): Drawable? {
//        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
//            .firstOrNull { it.name == app.name }
//            ?.let { packageManager.getApplicationIcon(it) }
        return null
    }
}