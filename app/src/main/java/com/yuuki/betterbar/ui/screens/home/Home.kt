package com.yuuki.betterbar.ui.screens.home

import android.content.SharedPreferences
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yuuki.betterbar.ui.dialog.BlurDialog
import com.yuuki.betterbar.ui.dialog.DialogState
import com.yuuki.betterbar.ui.theme.backgroundColor
import com.yuuki.betterbar.utils.putBoolean
import com.yuuki.betterbar.utils.rememberSharedPreferences


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainContent(xp_text: String, xp_color: Color, refreshSystemui: () -> Unit = {}, onConfirmClick: () -> Unit = {}) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = backgroundColor,
        topBar = {
            LargeTopAppBar(
                title = { Text("PureStatusBar") },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = backgroundColor)
            )
        },
        floatingActionButton = {
            var showDialog by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .size(80.dp, 56.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp)) 
                    .background(color = MaterialTheme.colorScheme.primary)
                    .combinedClickable( 
                        onClick = {
                            showDialog = true
                        },
                        onLongClick = refreshSystemui
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            if (showDialog) {
                BlurDialog(
                    onDismissRequest = { showDialog = false },
                    dialogState = DialogState(
                        title = "Yuuki",
                        content = "This module is completely free. if you purchased it, please get a refund. Please do not use it for illegal purposes. All responsibility does not belong to the author.",
                        onConfirmClick = onConfirmClick
                    )
                )
            }
        },


        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    is_Xposed(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        text = xp_text,
                        color = xp_color
                    )
                }

                item {
                    Text(
                        "Hide Icon",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(15.dp, 0.dp, 0.dp, 8.dp)
                    )
                    SettingList(
                        settings = listOf(
                            SettingItem("hide_wifi", "hide wifi"),
                            SettingItem("hide_mobile", "hide mobile"),
                            SettingItem("hide_app_icon", "hide app icon"),
                            SettingItem("hide_battery", "hide battery"),
                            SettingItem("hide_charge_indicator", "hide charge indicator"),
                            SettingItem("hide_low_speed", "hide low speed"),
                            SettingItem("hide_statusbar", "hide statusbar"),
                            SettingItem("hide_icon_bluetooth", "hide bluetooth"),
                            SettingItem("hide_icon_vpn", "hide vpn"),
                            SettingItem("hide_icon_airplane", "hide airplane"),
                            SettingItem("hide_icon_alarm_clock", "hide alarm clock"),
                            SettingItem("hide_icon_no_sims", "hide no sims"),
                            SettingItem("hide_icon_ethernet", "hide ethernet"),
                            SettingItem("hide_icon_nfc", "hide nfc"),
                            SettingItem("hide_icon_tty", "hide tty"),
                            SettingItem("hide_icon_zen", "hide zen"),
                            SettingItem("hide_icon_volume", "hide volume"),
                            SettingItem("hide_icon_mute", "hide mute"),
                            SettingItem("hide_icon_cast", "hide cast"),
                            SettingItem("hide_icon_data_saver", "hide data saver"),
                            SettingItem("hide_icon_microphone", "hide microphone"),
                            SettingItem("hide_icon_camera", "hide camera"),
                            SettingItem("hide_icon_location", "hide location"),
                            SettingItem("hide_icon_sensors_off", "hide sensors off"),
                            SettingItem("hide_icon_screen_record", "hide screen record"),
                            SettingItem("hide_icon_rotate", "hide rotate"),
                            SettingItem("hide_icon_headset", "hide headset")
                        )
                    )
                }

                item {
                    Text(
                        "Show Info",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(15.dp, 0.dp, 0.dp, 8.dp)
                    )
                    SettingList(
                        settings = listOf(
                            SettingItem("bold_time", "bold time"),
                            SettingItem("funny_time_style", "funny time style"),
                            SettingItem("show_temperature", "show temperature"),
                            SettingItem("show_power", "show power"),
                            SettingItem("status_lock", "status lock")

                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.size(56.dp))
                }
            }
        }
    )
}

@Composable
fun SettingCard(
    settings: List<SettingItem>,
    prefs: SharedPreferences,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            settings.forEach { setting ->
                SettingRow(
                    key = setting.key,
                    displayText = setting.displayText,
                    prefs = prefs
                )
            }
        }
    }
}

@Composable
fun SettingList(settings: List<SettingItem>) {
    val prefs = rememberSharedPreferences()
    SettingCard(settings = settings, prefs = prefs)
}

@Composable
fun SettingRow(
    key: String,
    displayText: String,
    prefs: SharedPreferences,
    modifier: Modifier = Modifier
) {
    val checkedState = remember { mutableStateOf(prefs.getBoolean(key, false)) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp, 0.dp, 0.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(0.dp))
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = checkedState.value,
            onCheckedChange = { newValue ->
                checkedState.value = newValue
                prefs.putBoolean(key, newValue) /
            },
            modifier = Modifier.scale(0.8f)
        )
    }
}

@Composable
fun is_Xposed(modifier: Modifier, text: String, color: Color) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(), 
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold, 
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                    fontFamily = FontFamily.SansSerif 
                ),
                color = color
            )
        }
    }
}

data class SettingItem(val key: String, val displayText: String)
