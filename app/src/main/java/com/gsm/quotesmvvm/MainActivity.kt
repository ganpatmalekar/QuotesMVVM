package com.gsm.quotesmvvm

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.gsm.quotesmvvm.repository.Response
import com.gsm.quotesmvvm.viewmodels.QuoteViewModel
import com.gsm.quotesmvvm.viewmodels.QuoteViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var quoteViewModel: QuoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val repository = (application as QuoteApplication).quoteRepository
        quoteViewModel = ViewModelProvider(
            this,
            QuoteViewModelFactory(repository)
        )[QuoteViewModel::class.java]

        quoteViewModel.quotes.observe(this) {
            when (it) {
                is Response.Loading -> {
                    Log.d("QuotesMVVM", "Response -> Loading...")
                }

                is Response.Success -> {
                    it.data?.let { quotes ->
                        Log.d("QuotesMVVM", "Response -> Quotes Size: ${quotes.results.size}")
                    }
                }

                is Response.Error -> {
                    Log.e("QuotesMVVM", "Response -> Error: ${it.errorMessage}")
                }
            }
        }


    }
}