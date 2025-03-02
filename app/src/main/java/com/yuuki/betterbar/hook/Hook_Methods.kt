package com.yuuki.betterbar

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.yuuki.betterbar.utils.TimeConvert
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.Calendar

class Hook_Methods {
    fun hide_wifi(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "com.android.systemui.statusbar.StatusBarWifiView",  // 类名
            lpparam.classLoader,
            "applyWifiState",
            XposedHelpers.findClass(
                "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$WifiIconState",
                lpparam.classLoader),

            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    XposedHelpers.setObjectField(param.args[0], "visible", false)

                }
            }
        )
    }

    fun hide_mobile(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "com.android.systemui.statusbar.StatusBarMobileView",  // 类名
            lpparam.classLoader,
            "applyMobileState",
            XposedHelpers.findClass(
                "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$MobileIconState",
                lpparam.classLoader),

            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val getSim = param.args[0]
                    XposedHelpers.setObjectField(getSim, "visible", false)

                }
            }
        )
    }

    fun hide_app_icon(lpparam: XC_LoadPackage.LoadPackageParam){
        XposedHelpers.findAndHookMethod(
            "com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment",
            lpparam.classLoader,
            "showNotificationIconArea",
            Boolean::class.java,
            object : XC_MethodReplacement() {
                @Throws(Throwable::class)
                override fun replaceHookedMethod(param: MethodHookParam): Any? {
                    return false
                }
            })
    }

    fun hide_battery(lpparam: XC_LoadPackage.LoadPackageParam){
        XposedHelpers.findAndHookMethod(
            "com.flyme.statusbar.battery.FlymeBatteryMeterView",
            lpparam.classLoader,
            "apply",
            Boolean::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val batteryView = param.thisObject as View
                    batteryView.visibility = View.GONE
                    XposedBridge.log("FlymeBatteryMeterView apply: Set to GONE, instance=${batteryView.hashCode()}")
                }
            }
        )
    }

    fun hide_charge_indicator(lpparam: XC_LoadPackage.LoadPackageParam){
        XposedHelpers.findAndHookMethod(
            "com.flyme.statusbar.battery.FlymeBatteryMeterView",
            lpparam.classLoader,
            "apply",
            Boolean::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val batteryView = param.thisObject as View
                    // 强制将 mCharging 和 mLastPlugged 设置为 false
                    XposedHelpers.setBooleanField(batteryView, "mCharging", false)
                    XposedHelpers.setBooleanField(batteryView, "mLastPlugged", false)
                    XposedBridge.log("FlymeBatteryMeterView apply: mCharging=false, mLastPlugged=false, instance=${batteryView.hashCode()}")
                }
            }
        )
    }

    fun hide_low_speed(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "com.flyme.statusbar.connectionRateView.ConnectionRateView",
            lpparam.classLoader,
            "updateConnectionRate",
            Double::class.javaPrimitiveType, // 确保方法参数类型匹配
            object : XC_MethodHook() {
                @Throws(Throwable::class)

                override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    val rate = param.args[0] as Double
                    XposedBridge.log("After updateConnectionRate: rate = $rate")

                    val view = XposedHelpers.getObjectField(param.thisObject, "mUnitView") as View
                    view.visibility = if (rate < 200) View.GONE else View.VISIBLE
                }
            }
        )
    }

    //隐藏状态栏
    fun hide_statusbar(lpparam: XC_LoadPackage.LoadPackageParam){

        XposedHelpers.findAndHookMethod(
            "com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment",
            lpparam.classLoader,
            "shouldHideStatusBar",
            object : XC_MethodReplacement() {
                @Throws(Throwable::class)
                override fun replaceHookedMethod(param: MethodHookParam): Any? {
                    return true
                }
            })
    }

    fun hide_icon(lpparam: XC_LoadPackage.LoadPackageParam, slot: String) {
        XposedHelpers.findAndHookMethod(
            "com.android.systemui.statusbar.phone.StatusBarIconControllerImpl",  // 类名
            lpparam.classLoader,
            "setIconVisibility",
            String::class.java,
            Boolean::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if(param.args[0].equals(slot)){
                        param.args[1] = false
                    }
                }
            }
        )
    }


    fun bold_time(lpparam: XC_LoadPackage.LoadPackageParam){

        val clockClass = XposedHelpers.findClass("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader)

        XposedBridge.hookAllConstructors(clockClass, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val clockView = param.thisObject as TextView
                clockView.setTypeface(null, Typeface.BOLD)
            }
        })

    }

    fun funny_time_style(lpparam: XC_LoadPackage.LoadPackageParam){

        val clockClass = XposedHelpers.findClass("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader)

        XposedHelpers.findAndHookMethod(
            clockClass,
            "getSmallTime", // 目标方法名
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)

                    val timeConvert = TimeConvert()
                    val CN_time = timeConvert.timeToShiChen(hour, minute)

                    param.result = CN_time
                }
            }
        )
    }


    fun show_info(lpparam: XC_LoadPackage.LoadPackageParam, text:String) {
        val TAG = "ConnectionRateEnhanceHook"
        val TEXT_VIEW_TAG_ID = 0x12345678

        // Hook ConnectionRateView 的 onAttachedToWindow 方法
        XposedHelpers.findAndHookMethod(
            "com.flyme.statusbar.connectionRateView.ConnectionRateView",
            lpparam.classLoader,
            "onAttachedToWindow",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val connectionRateView = param.thisObject as View
                    val parent = connectionRateView.parent as? ViewGroup ?: return
                    val context = connectionRateView.context

                    // 避免重复添加
                    if (parent.findViewWithTag<TextView>(TEXT_VIEW_TAG_ID) != null) return

                    // 创建并插入 TextView
                    val customTextView = createCustomTextView(context)
                    customTextView.tag = TEXT_VIEW_TAG_ID // 标记已添加
                    val index = parent.indexOfChild(connectionRateView)
                    parent.addView(customTextView, index + 1) // 在左侧插入

                    // 同步初始颜色
                    val currentColor = XposedHelpers.getIntField(connectionRateView, "mCurrentColor") as Int
                    customTextView.setTextColor(currentColor)

                    // 开始更新数据
                    updateTextView(customTextView, text)

                }
            }
        )

        // Hook onDarkChanged 以同步颜色（备用）
        XposedHelpers.findAndHookMethod(
            "com.flyme.statusbar.connectionRateView.ConnectionRateView",
            lpparam.classLoader,
            "onDarkChanged",
            ArrayList::class.java, Float::class.javaPrimitiveType, Int::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val newColor = XposedHelpers.getIntField(param.thisObject, "mCurrentColor") as Int
                    val parent = (param.thisObject as View).parent as? ViewGroup ?: return
                    val customTextView = parent.findViewWithTag<TextView>(TEXT_VIEW_TAG_ID)
                    customTextView?.setTextColor(newColor)
                    XposedBridge.log("$TAG: Color updated to $newColor")
                }
            }
        )
    }

    fun status_lock(lpparam: XC_LoadPackage.LoadPackageParam){
        XposedHelpers.findAndHookMethod(
            "com.android.systemui.statusbar.phone.PhoneStatusBarView",
            lpparam.classLoader,
            "onFinishInflate",
            object : XC_MethodHook() {
                var preTime = 0L
                override fun afterHookedMethod(param: MethodHookParam) {
                    val statusVarView = param.thisObject as ViewGroup
                    statusVarView.setOnTouchListener { view, event ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            val currTime = System.currentTimeMillis()
                            if (currTime - preTime <= 200) {
                                XposedHelpers.callMethod(
                                    view.context.getSystemService(Context.POWER_SERVICE),
                                    "goToSleep",
                                    SystemClock.uptimeMillis()
                                )
                            }
                            preTime = currTime
                        }
                        view.performClick()
                        return@setOnTouchListener false
                    }
                }
            })
    }


    // 创建自定义 TextView
    fun createCustomTextView(context: Context): TextView {
        val textView = TextView(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT // 高度与父容器一致
        )
        textView.layoutParams = lp
        textView.textSize = 12f
        textView.setPadding(8, 0, 8, 0)
        textView.gravity = android.view.Gravity.CENTER_VERTICAL
        textView.text = "0.0" //init
        return textView
    }

    fun updateTextView(textView: TextView, text:String) {
        val handler = Handler(Looper.getMainLooper()) { msg ->
            if (msg.what == 1001) {
                val newText = msg.obj as String
                textView.text = newText
                XposedBridge.log("Updating TextView with: $newText")
            }
            true
        }

        Thread {
            while (true) {
                val newText = text
                handler.sendMessage(handler.obtainMessage(1001, newText))
                Thread.sleep(2000)
            }
        }.start()
    }

}
