package com.yuuki.betterbar.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.yuuki.betterbar.ui.theme.dialog_background
import com.yuuki.betterbar.ui.theme.dialog_button_clolr


// 定义状态数据类，用于传递标题、正文和确认点击事件
data class DialogState(
    val title: String = "对话框标题",
    val content: String = "这里是对话框的正文内容，可以包含更多信息或描述。",
    val onConfirmClick: () -> Unit = {}
)

@Composable
fun BlurDialog(
    onDismissRequest: () -> Unit,
    dialogState: DialogState
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // 让弹窗宽度可自定义
        )
    ) {
        // 应用模糊效果
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        DialogBlurBehindUtils.setupWindowBlurListener(dialogWindowProvider.window)

        // 弹窗内容
        Surface(
            modifier = Modifier
                .width(330.dp)
                .height(220.dp),
            shape = RoundedCornerShape(32.dp),
            color = dialog_background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // 标题（左上角）
                Text(
                    text = dialogState.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.Start) // 左对齐
                        .padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp)) // 减少标题与正文的间距

                // 正文（左对齐）
                Text(
                    text = dialogState.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start, // 左对齐
                    modifier = Modifier
                        .align(Alignment.Start) // 左对齐
                        .padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f)) // 填充剩余空间，使按钮下沉

                // 按钮（右下角）
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End), // 右对齐
                    horizontalArrangement = Arrangement.End // 按钮靠右
                ) {
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = dialog_button_clolr, // 设置“取消”按钮背景为灰色
                        )
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = dialogState.onConfirmClick,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("JoinQQ")
                    }
                }
            }
        }
    }
}
