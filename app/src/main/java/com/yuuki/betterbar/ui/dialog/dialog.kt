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


data class DialogState(
    val title: String = "title",
    val content: String = "text",
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
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        DialogBlurBehindUtils.setupWindowBlurListener(dialogWindowProvider.window)

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
                Text(
                    text = dialogState.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = dialogState.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start, 
                    modifier = Modifier
                        .align(Alignment.Start) 
                        .padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f)) 

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End), 
                    horizontalArrangement = Arrangement.End 
                ) {
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = dialog_button_clolr, 
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
