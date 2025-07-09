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

    private val quotesLiveData = MutableLiveData<Response<QuoteList>>()
    val quotes: LiveData<Response<QuoteList>>
        get() = quotesLiveData

    suspend fun getQuotes(page: Int) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            quotesLiveData.postValue(Response.Loading())
            try {
                val result = quoteService.getQuotes(page)
                if (result.body() != null) {
                    result.body()?.let { quoteDatabase.quoteDao().addQuotes(it.results) }
                    quotesLiveData.postValue(Response.Success(result.body()))
                } else {
                    quotesLiveData.postValue(
                        Response.Error(
                            "API failed with status code ${result.code()}"
                        )
                    )
                }
            } catch (e: Exception) {
                quotesLiveData.postValue(Response.Error(e.message.toString()))
            }
        } else {
            try {
                val quotes = quoteDatabase.quoteDao().getQuotes()
                val quoteList = QuoteList(1, 1, 1, quotes, 10, 10)
                quotesLiveData.postValue(Response.Success(quoteList))
            } catch (e: Exception) {
                quotesLiveData.postValue(Response.Error(e.message.toString()))
            }
        }
    }

    /*
    Method to get quotes from random page
     */
    suspend fun getQuotesBackground() {
        val randomNumber = (Math.random() * 10).toInt()
        val result = quoteService.getQuotes(randomNumber)
        if (result.body() != null) {
            result.body()?.let { quoteDatabase.quoteDao().addQuotes(it.results) }
        }
    }
}