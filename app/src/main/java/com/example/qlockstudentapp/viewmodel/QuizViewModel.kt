package com.example.qlockstudentapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlockstudentapp.api.ApiClient
import com.example.qlockstudentapp.model.request.Answer
import com.example.qlockstudentapp.model.request.QuizSubmissionRequest
import com.example.qlockstudentapp.model.response.Quiz
import com.example.qlockstudentapp.model.response.QuizSubmissionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Corrected QuizViewModel
 *
 * Main fixes / changes made:
 *  - Use nullable Quiz? state via mutableStateOf instead of the incorrect mutableLongStateOf usage.
 *  - Expose selectedAnswers as an immutable Map while keeping an internal mutableStateMap.
 *  - Improved timer start/stop logic using a Job and checking isTimerRunning correctly.
 *  - Use Dispatchers.IO for network calls (withContext) so we don't block the main thread.
 *  - Better null-safety and error reporting (calls to onSuccess only on success, sets errorMessage on failure).
 */
class QuizViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "QuizViewModel"
    }

    // Quiz Data
    var quiz: Quiz? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var errorMessage: String by mutableStateOf("")
        private set

    // Answers State (QuestionId → SelectedOptionId)
    private val _selectedAnswers = mutableStateMapOf<Long, Long>()
    // expose as read-only map
    val selectedAnswers: Map<Long, Long> get() = _selectedAnswers

    // Timer State
    var timeLeft: Long by mutableLongStateOf(0L)
        private set

    var isTimerRunning: Boolean by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    private fun clearError() {
        errorMessage = ""
    }

    /**
     * Fetch Quiz by Access Code
     * onSuccess is invoked only when quiz data is successfully loaded
     */
    fun fetchQuiz(accessCode: String, onSuccess: () -> Unit) {
        if (accessCode.isBlank()) {
            errorMessage = "Access code cannot be empty"
            return
        }

        isLoading = true
        clearError()

        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext

                // perform network call on IO
                val response = withContext(Dispatchers.IO) {
                    ApiClient.getApiService(context).getQuizByAccessCode(accessCode)
                }

                if (response.isSuccessful && response.body() != null) {
                    val quizData = response.body()!!.quiz
                    quiz = quizData

                    // set timer (minutes → seconds)
                    timeLeft = quizData.time_limit_minutes.toLong() * 60L

                    onSuccess()
                    Log.d(TAG, "Quiz loaded: ${'$'}{quizData.title}")
                } else {
                    errorMessage = "Invalid or expired access code"
                    Log.e(TAG, "Fetch failed: ${'$'}{response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Network error. Check your connection."
                Log.e(TAG, "Exception: ${'$'}{e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }

    // Select Answer
    fun selectAnswer(questionId: Long, optionId: Long) {
        _selectedAnswers[questionId] = optionId
    }

    // Submit Quiz
    fun submitQuiz(onSuccess: (QuizSubmissionResponse) -> Unit, onError: (String) -> Unit) {
        val currentQuiz = quiz
        if (currentQuiz == null) {
            val msg = "No quiz loaded"
            onError(msg)
            Log.e(TAG, msg)
            return
        }

        val answersList = selectedAnswers.map { (qid, oid) ->
            Answer(question_id = qid, selected_option_id = oid)
        }

        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val request = QuizSubmissionRequest(
                    quiz_id = currentQuiz.id,
                    answers = answersList
                )

                val response = withContext(Dispatchers.IO) {
                    ApiClient.getApiService(context).submitQuiz(request)
                }

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    onSuccess(body)
                    Log.d(TAG, "Quiz submitted successfully. Score: ${'$'}{body.score}")
                } else {
                    val error = try {
                        response.errorBody()?.string()
                    } catch (e: Exception) {
                        null
                    }
                    val errMsg = "Failed to submit quiz: ${"Unknown Error"}"
                    onError(errMsg)
                    Log.e(TAG, "Submit failed: ${'$'}{error}")
                }
            } catch (e: Exception) {
                val msg = "Network error: ${'$'}{e.message}"
                onError(msg)
                Log.e(TAG, "Exception: ${'$'}{e.message}", e)
            }
        }
    }

    /**
     * Start the countdown timer. If already running, the call is ignored.
     * onTimeUp is invoked once when timeLeft reaches 0.
     */
// Remove the default = null
    fun startTimer(onTimeUp: () -> Unit) {
        if (isTimerRunning && timerJob?.isActive == true) return

        isTimerRunning = true
        timerJob = viewModelScope.launch {
            while (isTimerRunning && timeLeft > 0) {
                delay(1000)
                timeLeft -= 1
            }
            if (timeLeft <= 0L) {
                isTimerRunning = false
                onTimeUp() // Now safe to call directly
            }
        }
    }

    // Stop Timer
    fun stopTimer() {
        isTimerRunning = false
        timerJob?.cancel()
        timerJob = null
    }

    //format Time
}
