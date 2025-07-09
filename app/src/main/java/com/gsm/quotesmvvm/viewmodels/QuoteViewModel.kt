package com.gsm.quotesmvvm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.quotesmvvm.models.QuoteList
import com.gsm.quotesmvvm.repository.QuoteRepository
import com.gsm.quotesmvvm.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val randomNumber = (Math.random() * 10).toInt()
            repository.getQuotes(randomNumber)
        }
    }

    val quotes: LiveData<Response<QuoteList>>
        get() = repository.quotes
}