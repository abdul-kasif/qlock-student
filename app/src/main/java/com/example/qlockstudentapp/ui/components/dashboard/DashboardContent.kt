package com.example.qlockstudentapp.ui.components.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qlockstudentapp.viewmodel.DashboardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel(),
    onJoinTest: (String) -> Unit,
    onInvalidAccessCode: () -> Unit
) {
    dashboardViewModel.loadDashboard()
    val dashboardData by dashboardViewModel::dashboardData
    val isLoading by dashboardViewModel::isLoading
    val errorMessage by dashboardViewModel::errorMessage

    LazyColumn(
        modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        if (isLoading) {
            item { DashboardLoading() }
        } else if (errorMessage.isNotEmpty()) {
            item { DashboardError(errorMessage = errorMessage) }
        } else if (dashboardData != null) {
            val user = dashboardData!!.user

            item { WelcomeMessage(name = user.name) }
            item { StudentInfoCard(user = user) }
            item { JoinTestSection(onJoinTest, onInvalidAccessCode) }
            item { TestHistoryHeader() }
            items(dashboardData!!.taken_tests) { test ->
                TestHistoryCard(
                    title = test.title,
                    status = test.status,
                    startedAt = test.started_at,
                    submittedAt = test.submitted_at
                )
            }
            item { Spacer(modifier.height(48.dp)) }
        }
    }
}