package com.yuuki.betterbar.hook

import com.yuuki.betterbar.Hook_Methods
import com.yuuki.betterbar.utils.HardwareInfo
import com.yuuki.betterbar.utils.XPSharedPreferences
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {

    private val ICON_CONFIGS = mapOf(
        "hide_icon_bluetooth" to "bluetooth",
        "hide_icon_vpn" to "vpn",
        "hide_icon_airplane" to "airplane",
        "hide_icon_alarm_clock" to "alarm_clock",
        "hide_icon_no_sims" to "no_sims",
        "hide_icon_ethernet" to "ethernet",
        "hide_icon_nfc" to "nfc",
        "hide_icon_tty" to "tty",
        "hide_icon_zen" to "zen",
        "hide_icon_volume" to "volume",
        "hide_icon_mute" to "mute",
        "hide_icon_cast" to "cast",
        "hide_icon_data_saver" to "data_saver",
        "hide_icon_microphone" to "microphone",
        "hide_icon_camera" to "camera",
        "hide_icon_location" to "location",
        "hide_icon_sensors_off" to "sensors_off",
        "hide_icon_screen_record" to "screen_record",
        "hide_icon_rotate" to "rotate",
        "hide_icon_headset" to "headset"
    )

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        //check is active
        if(lpparam.packageName.equals("com.yuuki.betterbar")){
            XposedHelpers.findAndHookMethod("com.yuuki.betterbar.MainActivity",lpparam.classLoader,
                "isModuleActive", XC_MethodReplacement.returnConstant(true));
        }

        val hook_list = Hook_Methods()
        val xsp = XPSharedPreferences()
        val hardwareInfo = HardwareInfo()

        if (lpparam.packageName.equals("com.android.systemui")) {
            if(xsp.getValue("hide_wifi"))
                hook_list.hide_wifi(lpparam)
            if(xsp.getValue("hide_mobile"))
                hook_list.hide_mobile(lpparam)
            if(xsp.getValue("hide_app_icon"))
                hook_list.hide_app_icon(lpparam)
            if(xsp.getValue("hide_battery"))
                hook_list.hide_battery(lpparam)
            if(xsp.getValue("hide_charge_indicator"))
                hook_list.hide_charge_indicator(lpparam)
            if(xsp.getValue("hide_low_speed"))
                hook_list.hide_low_speed(lpparam)
            if(xsp.getValue("hide_statusbar"))
                hook_list.hide_statusbar(lpparam)

            ICON_CONFIGS.forEach { (configKey, iconName) ->
                val shouldHide = xsp.getValue(configKey)
                if (shouldHide) {
                    hook_list.hide_icon(lpparam, iconName)
                }
            }

            if(xsp.getValue("bold_time"))
                hook_list.bold_time(lpparam)
            if(xsp.getValue("funny_time_style"))
                hook_list.funny_time_style(lpparam)
            if(xsp.getValue("show_temperature"))
                hardwareInfo.getCpuAverageTemperature()?.let { hook_list.show_info(lpparam, it) }
            if(xsp.getValue("show_power"))
                hook_list.show_info(lpparam, hardwareInfo.getBatteryPowerNow())
            if(xsp.getValue("status_lock"))
                hook_list.status_lock(lpparam)

        }

    }


}