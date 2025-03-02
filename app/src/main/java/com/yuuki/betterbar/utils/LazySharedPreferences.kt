package com.yuuki.betterbar.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberSharedPreferences(): SharedPreferences {
    val context = LocalContext.current
    return remember {
        context.getSharedPreferences("yuuki_prefs", Context.MODE_WORLD_READABLE)
    }
}

fun SharedPreferences.getBoolean(key: String, default: Boolean): Boolean {
    return getBoolean(key, default)
}

fun SharedPreferences.putBoolean(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}