// utils/TimerUtils.kt
package com.example.qlockstudentapp.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class CountDownTimer(
    private val totalSeconds: Long,
    private val onTick: (Long) -> Unit,
    private val onFinish: () -> Unit
) {
    private var job: Job? = null
    private val isRunning = AtomicBoolean(false)

    fun start() {
        if (isRunning.get()) return
        isRunning.set(true)
        job = CoroutineScope(Dispatchers.Main).launch {
            var secondsLeft = totalSeconds
            while (secondsLeft > 0 && isRunning.get()) {
                onTick(secondsLeft)
                delay(1000)
                secondsLeft--
            }
            if (isRunning.get()) {
                onFinish()
            }
        }
    }

    fun cancel() {
        isRunning.set(false)
        job?.cancel()
    }
}