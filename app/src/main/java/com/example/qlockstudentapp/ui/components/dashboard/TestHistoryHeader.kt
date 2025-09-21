// ui/components/dashboard/TestHistoryHeader.kt
package com.example.qlockstudentapp.ui.components.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TestHistoryHeader(modifier: Modifier = Modifier) {
    Text(
        text = "Your Test History",
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(vertical = 8.dp)
    )
}