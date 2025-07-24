package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nsergio.dev.myinstagramcompose.core.ui.DimensDP
import com.nsergio.dev.myinstagramcompose.core.ui.DimensSp

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(DimensDP.DP48.dp),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(DimensDP.DP12.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = DimensSp.SP14.sp
            )
        )
    }
}