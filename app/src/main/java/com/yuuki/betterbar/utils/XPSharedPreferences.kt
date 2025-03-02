package com.yuuki.betterbar.utils

import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge

class XPSharedPreferences {
    private var prefs: XSharedPreferences

    init {
        prefs = XSharedPreferences("com.yuuki.betterbar", "yuuki_prefs")
        prefs.makeWorldReadable()
    }

    fun getValue(key: String): Boolean {
        prefs.reload()
        val value = prefs.getBoolean(key, false)
        XposedBridge.log("Value for key $key: $value")
        return value
    }

}
