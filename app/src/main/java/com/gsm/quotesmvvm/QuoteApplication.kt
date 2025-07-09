package com.gsm.quotesmvvm

import android.app.Application
import com.gsm.quotesmvvm.api.QuoteService
import com.gsm.quotesmvvm.api.RetrofitHelper
import com.gsm.quotesmvvm.db.QuoteDatabase
import com.gsm.quotesmvvm.repository.QuoteRepository

class QuoteApplication : Application() {

    lateinit var quoteRepository: QuoteRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)
        val quoteDatabase = QuoteDatabase.getDatabase(applicationContext)
        quoteRepository = QuoteRepository(quoteService, quoteDatabase, applicationContext)
    }
}