package com.example.qlockstudentapp.api

import com.example.qlockstudentapp.model.request.ProfileSetupRequest
import com.example.qlockstudentapp.model.request.SendOtpRequest
import com.example.qlockstudentapp.model.request.VerifyOtpRequest
import com.example.qlockstudentapp.model.response.ApiResponse
import com.example.qlockstudentapp.model.response.ProfileResponse
import com.example.qlockstudentapp.model.response.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/send_otp")
    suspend fun sentOtp(@Body request: SendOtpRequest): Response<ApiResponse>

    @POST("auth/verify_otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("profile")
    suspend fun setupProfile(@Body request: ProfileSetupRequest): Response<ProfileResponse>
}

