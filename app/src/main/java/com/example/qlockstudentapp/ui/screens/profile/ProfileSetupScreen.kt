// ui/screens/Profile/ProfileSetupScreen.kt
package com.example.qlockstudentapp.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ProfileSetupScreen(navController: NavHostController) {
    var userPersonalId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Complete Your Profile",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        OutlinedTextField(
            value = userPersonalId,
            onValueChange = { userPersonalId = it },
            label = { Text("Student ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            label = { Text("Department") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                // TODO: Call /profile API and navigate to Dashboard
                navController.navigate("dashboard") {
                    popUpTo("profile_setup") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Profile")
        }
    }
}