package com.example.campqstudent.api

import com.example.campqstudent.model.request.ProfileSetupRequest
import com.example.campqstudent.model.request.QuizSubmissionRequest
import com.example.campqstudent.model.request.SendOtpRequest
import com.example.campqstudent.model.request.VerifyOtpRequest
import com.example.campqstudent.model.response.ApiResponse
import com.example.campqstudent.model.response.ProfileResponse
import com.example.campqstudent.model.response.QuizAccessResponse
import com.example.campqstudent.model.response.QuizSubmissionResponse
import com.example.campqstudent.model.response.QuizValidateResponse
import com.example.campqstudent.model.response.StudentDashboardResponse
import com.example.campqstudent.model.response.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/send_otp")
    suspend fun sentOtp(@Body request: SendOtpRequest): Response<ApiResponse>

    @POST("auth/verify_otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("profile")
    suspend fun setupProfile(@Body request: ProfileSetupRequest): Response<ProfileResponse>

    @GET("dashboard")
    suspend fun getStudentDashboard(): Response<StudentDashboardResponse>

    @GET("student_quizzes/access/{access_code}")
    suspend fun checkCodeValidity(
        @Path("access_code") accessCode: String
    ): Response<QuizValidateResponse>


    @POST("quiz_submissions")
    suspend fun submitQuiz(
        @Body request: QuizSubmissionRequest
    ): Response<QuizSubmissionResponse>

    @GET("quiz_submissions/{id}")
    suspend fun getQuizByAccessCode(
        @Path("id") accessCode: String
    ): Response<QuizAccessResponse>
}

