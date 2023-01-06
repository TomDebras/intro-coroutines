package contributors

import java.util.prefs.Preferences

private fun prefNode(): Preferences = Preferences.userRoot().node("ContributorsUI")

data class Params(val token: String, val org: String, val variant: Variant)

fun loadStoredParams(): Params {
    return prefNode().run {
        Params(
            get("token", ""),
            get("org", "kotlin"),
            Variant.valueOf(get("variant", Variant.BLOCKING.name))
        )
    }
}

fun removeStoredParams() {
    prefNode().removeNode()
}

fun saveParams(params: Params) {
    prefNode().apply {
        put("token", params.token)
        put("org", params.org)
        put("variant", params.variant.name)
        sync()
    }
}