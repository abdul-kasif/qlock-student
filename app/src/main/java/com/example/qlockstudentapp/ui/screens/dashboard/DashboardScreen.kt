// ui/screens/Dashboard/DashboardScreen.kt
package com.example.qlockstudentapp.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "QLock",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Button(onClick = { /* TODO: Logout */ }) {
            Text("Logout")
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Welcome to your Dashboard!",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}