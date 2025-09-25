// ui/screens/quiz/QuizResultActivity.kt
package com.example.qlockstudentapp.ui.screens.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import com.example.qlockstudentapp.ui.theme.QLockStudentAppTheme

class QuizResultActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_SCORE = "extra_score"

        fun start(context: Context, score: Int) {
            val intent = Intent(context, QuizResultActivity::class.java).apply {
                putExtra(EXTRA_SCORE, score)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val score = intent.getIntExtra(EXTRA_SCORE, 0)

        setContent {
            QLockStudentAppTheme {
                // Reuse your composable
                QuizResultScreen(
                    navController = NavHostController(this),
                    score = score
                )
            }
        }
    }
}
