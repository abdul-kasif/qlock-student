// ui/components/dashboard/JoinTestSection.kt
package com.example.qlockstudentapp.ui.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun JoinTestSection(
    onJoinTest: (String) -> Unit,
    onInvalidAccessCode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Join a New Test",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        var accessCode by remember { mutableStateOf("") }

        OutlinedTextField(
            value = accessCode,
            onValueChange = { accessCode = it },
            label = { Text("Enter 6-digit Access Code") },
            placeholder = { Text("e.g. XK9Q2M") },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (accessCode.length == 6) {
                    onJoinTest(accessCode)
                } else {
                    onInvalidAccessCode()
                }
            },
            enabled = accessCode.length == 6,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Join Test",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}