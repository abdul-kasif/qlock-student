package com.example.qlockstudentapp.api

import com.example.qlockstudentapp.utils.SecureStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: android.content.Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Skip adding token for public endpoints (like /auth/send_otp)
        if (request.url.encodedPath.startsWith("/auth/")) {
            return chain.proceed(request)
        }

        // Get token from secure storage
        val token = SecureStorage.getInstance(context).getToken()

        // If no token, proceed without auth (will likely fail with 401 — which is correct)
        val authenticatedRequest = if (token != null) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(authenticatedRequest)
    }
}