// ui/components/dashboard/WelcomeMessage.kt
package com.example.qlockstudentapp.ui.components.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeMessage(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Welcome back, $name!",
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.SemiBold
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(bottom = 16.dp)
    )
}