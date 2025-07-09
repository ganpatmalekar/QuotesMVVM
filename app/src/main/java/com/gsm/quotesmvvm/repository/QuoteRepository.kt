package com.gsm.quotesmvvm.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gsm.quotesmvvm.api.QuoteService
import com.gsm.quotesmvvm.db.QuoteDatabase
import com.gsm.quotesmvvm.models.QuoteList
import com.gsm.quotesmvvm.utils.NetworkUtils

class QuoteRepository(
    private val quoteService: QuoteService,
    private val quoteDatabase: QuoteDatabase,
    private val applicationContext: Context
) {

    private val quotesLiveData = MutableLiveData<QuoteList>()
    val quotes: LiveData<QuoteList>
        get() = quotesLiveData

    suspend fun getQuotes(page: Int) {

        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = quoteService.getQuotes(page)
            if (result.body() != null) {
                result.body()?.let { quoteDatabase.quoteDao().addQuotes(it.results) }
                quotesLiveData.postValue(result.body())
            }
        } else {
            try {
                val quotes = quoteDatabase.quoteDao().getQuotes()
                val quoteList = QuoteList(1, 1, 1, quotes, 10, 10)
                quotesLiveData.postValue(quoteList)
            } catch (e: Exception) {
                Log.e("QuotesMVVM", "Error in accessing db: ${e.message}")
            }
        }
    }
}