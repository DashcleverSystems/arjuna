package io.arjuna.apps

class InstalledApp(
    val name: String
) {

    override fun equals(other: Any?): Boolean {
        if (other !is InstalledApp) return false
        return other.name == this.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}