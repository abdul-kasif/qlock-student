package com.example.campqstudent.ui.components.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.campqstudent.model.response.TakenQuizResponse
import com.example.campqstudent.utils.formatDateReadable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuizHistoryCard(
    quiz: TakenQuizResponse,
    modifier: Modifier = Modifier
) {
    val statusColor = when (quiz.status.lowercase()) {
        "submitted" -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.secondary
    }

    val scoreColor = when {
        quiz.score >= 70 -> MaterialTheme.colorScheme.primary
        quiz.score >= 50 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Quiz Title
            Text(
                text = quiz.quiz_title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subject Info
            Text(
                text = "${quiz.subject_code} - ${quiz.subject_name}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Score Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = scoreColor.copy(alpha = 0.1f),
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = "${quiz.score}%",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    ),
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = statusColor.copy(alpha = 0.1f),
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = quiz.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = statusColor
                    ),
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Date Row with vertical divider
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Started",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Text(
                        text = formatDateReadable(quiz.started_at),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (quiz.submitted_at != null) "Submitted" else "Ended",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Text(
                        text = quiz.submitted_at?.let { formatDateReadable(it) } ?: "Not submitted",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (quiz.submitted_at != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
