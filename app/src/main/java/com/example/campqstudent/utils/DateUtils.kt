// utils/DateUtils.kt
package com.example.campqstudent.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateReadable(dateTimeString: String): String {
    return try {
        val clean = dateTimeString.substringBeforeLast("+").substringBeforeLast(".")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val localDateTime = LocalDateTime.parse(clean, formatter)
        val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
        localDateTime.format(outputFormatter)
    } catch (e: Exception) {
        dateTimeString.take(10).replace("-", "/")
    }
}