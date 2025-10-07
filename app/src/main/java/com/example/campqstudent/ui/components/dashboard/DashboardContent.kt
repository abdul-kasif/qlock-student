package com.example.campqstudent.ui.components.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.campqstudent.viewmodel.DashboardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel(),
    navController: NavHostController,
    onJoinTest: (String) -> Unit,
    onInvalidAccessCode: () -> Unit
) {
    val dashboardData = dashboardViewModel.dashboardData
    val isLoading = dashboardViewModel.isLoading
    val errorMessage = dashboardViewModel.errorMessage

    // 🚀 Trigger data load on first launch
    LaunchedEffect(Unit) {
        dashboardViewModel.loadDashboard()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        when {
            isLoading -> {
                item { DashboardLoading() }
            }
            errorMessage.isNotEmpty() -> {
                item { DashboardError(errorMessage = errorMessage) }
            }
            dashboardData != null -> {
                val user = dashboardData.user
                val quizzes = dashboardData.taken_quizzes

                item { WelcomeMessage(name = user.name) }
                item { StudentInfoCard(user = user) }
                item { JoinTestSection(onJoinTest, onInvalidAccessCode) }

                // Quiz History Header
                item {
                    Text(
                        text = "Your Quiz History",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // 🧠 Check if quiz history is empty
                if (quizzes.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(64.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "No quiz history yet",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "You haven’t taken any quizzes yet !",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(quizzes) { quiz ->
                        QuizHistoryCard(quiz = quiz)
                    }
                }

                item { Spacer(modifier = Modifier.height(48.dp)) }
            }
            else -> {
                item { DashboardLoading() }
            }
        }
    }
}
