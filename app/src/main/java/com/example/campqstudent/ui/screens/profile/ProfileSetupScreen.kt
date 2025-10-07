package com.example.campqstudent.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.campqstudent.viewmodel.ProfileSetupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    navController: NavHostController,
    profileSetupViewModel: ProfileSetupViewModel = viewModel()
) {
    val userPersonalId by profileSetupViewModel::userPersonalId
    val name by profileSetupViewModel::name
    val department by profileSetupViewModel::department
    val isLoading by profileSetupViewModel::isLoading
    val errorMessage by profileSetupViewModel::errorMessage
    // Observe the expanded state from the ViewModel
    val expanded by profileSetupViewModel::isDepartmentDropdownExpanded

    // Static department list (for now)
    val departments = listOf(
        "Information Technology",
        "Computer Science",
        "Electrical Engineering",
        "Mechanical Engineering",
        "Civil Engineering",
        "Business Administration",
        "Mathematics",
        "Physics",
        "Chemistry",
        "Biology"
    )

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
            onValueChange = profileSetupViewModel::onUserPersonalIdChanged,
            label = { Text("Student ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = profileSetupViewModel::onNameChanged,
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown for Department
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { profileSetupViewModel.onDepartmentDropdownExpandedChanged(!expanded) }
        ) {
            OutlinedTextField(
                value = department,
                onValueChange = { /* readOnly field, so no change is necessary here */ },
                readOnly = true, // It is a dropdown, so it should be readOnly
                label = { Text("Department") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor() // Correctly anchors the menu
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { profileSetupViewModel.onDepartmentDropdownExpandedChanged(false) }
            ) {
                departments.forEach { dept ->
                    DropdownMenuItem(
                        text = { Text(dept) },
                        onClick = {
                            profileSetupViewModel.onDepartmentChanged(dept)
                            profileSetupViewModel.onDepartmentDropdownExpandedChanged(false)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                profileSetupViewModel.submitProfile {
                    navController.navigate("dashboard") {
                        popUpTo("profile_setup") { inclusive = true }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Submit Profile")
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
