package com.helic.moviexy.utils

import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.helic.moviexy.presentation.ui.theme.*


@Composable
fun DisplayAlertDialog(
    title: String,
    message: @Composable (() -> Unit),
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onYesClicked: () -> Unit,
) {
    if (openDialog) {
        AlertDialog(
            backgroundColor = DarkerGray,
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = LightGray
                )
            },
            text = message,
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.ButtonColor),
                    onClick = {
                        onYesClicked()
                        closeDialog()
                    })
                {
                    Text(text = "Yes", color = LightGray)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { closeDialog() }, Modifier.background(Color.Transparent))
                {
                    Text(
                        text = "No",
                        color = MaterialTheme.colors.DialogNoText
                    )
                }
            },
            onDismissRequest = { closeDialog() }
        )
    }
}
