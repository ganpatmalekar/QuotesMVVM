package com.gsm.quotesmvvm.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gsm.quotesmvvm.QuoteApplication

class QuoteWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val repository = (context as QuoteApplication).quoteRepository
            repository.getQuotesBackground()
            Result.success()
        } catch (e: Exception) {
            Log.e("QuotesMVVM", "doWork: ${e.message}")
            Result.failure()
        }
    }
}