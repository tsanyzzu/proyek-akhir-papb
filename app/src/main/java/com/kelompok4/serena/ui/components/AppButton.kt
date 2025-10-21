package com.kelompok4.serena.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kelompok4.serena.ui.theme.*

// Enum untuk menentukan tipe button dengan lebih aman dan jelas
enum class ButtonType {
    PRIMARY,
    SECONDARY,
    TERTIARY
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: ButtonType = ButtonType.PRIMARY,
    enabled: Boolean = true
) {
    val buttonColors = when (buttonType) {
        ButtonType.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = Primary500,
            contentColor = Color.White,
            disabledContainerColor = DisabledGray,
            disabledContentColor = DisabledTextGray
        )
        ButtonType.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Primary500,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = DisabledTextGray
        )
        ButtonType.TERTIARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Primary500,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = DisabledTextGray
        )
    }

    val border = if (buttonType == ButtonType.SECONDARY && enabled) {
        BorderStroke(1.5.dp, Primary500)
    } else if (buttonType == ButtonType.SECONDARY && !enabled) {
        BorderStroke(1.5.dp, DisabledTextGray)
    } else {
        null
    }

    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = buttonColors,
        border = border,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(text = text, style = AppTypography.Body1.medium)
    }
}