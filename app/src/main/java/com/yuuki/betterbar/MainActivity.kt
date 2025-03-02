package com.yuuki.betterbar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yuuki.betterbar.ui.screens.home.MainContent
import com.yuuki.betterbar.ui.theme.BetterBarTheme
import com.yuuki.betterbar.ui.theme.activated
import com.yuuki.betterbar.ui.theme.inactivated
import java.io.DataOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BetterBarTheme {

                if(isModuleActive()){
                    MainContent(
                        "**** The module is already working normally \uD83D\uDE0B ****",
                        activated,
                        refreshSystemui = {
                            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                            refreshUI()
                        },
                        onConfirmClick = {
                            joinQQGroup("ScaxpD6_dYtj3zdHtSQTR_n6i1SZACx1")
                        }
                    )
                }

                else{
                    MainContent(
                        "**** Please activate the module first ☹\uFE0F ****",
                        inactivated,
                        refreshSystemui = {
                            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                            refreshUI()
                        },
                        onConfirmClick = {
                            joinQQGroup("ScaxpD6_dYtj3zdHtSQTR_n6i1SZACx1")
                        }
                    )
                }

            }
        }
    }

    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key"))
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent)
            return true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            return false
        }
    }

    fun refreshUI(){
        try {
            val root = Runtime.getRuntime().exec("su")
            val outputStream = root.outputStream
            val dataOutputStream = DataOutputStream(outputStream)
            dataOutputStream.writeBytes("pkill -f com.android.systemui")
            dataOutputStream.flush()
            dataOutputStream.close()
            outputStream.close()
        } catch (t: Throwable) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            t.printStackTrace()
        }
    }

    fun isModuleActive(): Boolean {
        return false
    }

}