package com.gsm.quotesmvvm

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.gsm.quotesmvvm.api.QuoteService
import com.gsm.quotesmvvm.api.RetrofitHelper
import com.gsm.quotesmvvm.db.QuoteDatabase
import com.gsm.quotesmvvm.repository.QuoteRepository
import com.gsm.quotesmvvm.worker.QuoteWorker
import java.util.concurrent.TimeUnit

class QuoteApplication : Application() {

    lateinit var quoteRepository: QuoteRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
        setupWorker()
    }

    private fun setupWorker() {
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workerRequest = PeriodicWorkRequest
            .Builder(QuoteWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance(this).enqueue(workerRequest)
    }

    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)
        val quoteDatabase = QuoteDatabase.getDatabase(applicationContext)
        quoteRepository = QuoteRepository(quoteService, quoteDatabase, applicationContext)
    }
}