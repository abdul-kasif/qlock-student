package com.example.qlockstudentapp.ui.components.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.qlockstudentapp.viewmodel.DashboardViewModel

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

                // Quiz History Cards
                items(dashboardData.taken_quizzes) { quiz ->
                    QuizHistoryCard(quiz = quiz)
                }

                item { Spacer(modifier = Modifier.height(48.dp)) }
            }
            else -> {
                // First time → show loading until API finishes
                item { DashboardLoading() }
            }
        }
    }
}
