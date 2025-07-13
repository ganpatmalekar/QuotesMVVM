package com.gsm.quotesmvvm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsm.quotesmvvm.adapters.QuoteListAdapter
import com.gsm.quotesmvvm.databinding.FragmentQuoteListBinding
import com.gsm.quotesmvvm.models.Result
import com.gsm.quotesmvvm.repository.Response
import com.gsm.quotesmvvm.viewmodels.QuoteViewModel
import com.gsm.quotesmvvm.viewmodels.QuoteViewModelFactory

class QuoteListFragment : Fragment() {

    private var _binding: FragmentQuoteListBinding? = null
    private val binding get() = _binding!!

    lateinit var quoteViewModel: QuoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuoteListBinding.inflate(inflater, container, false)

        val repository = (requireActivity().application as QuoteApplication).quoteRepository
        quoteViewModel = ViewModelProvider(
            this,
            QuoteViewModelFactory(repository)
        )[QuoteViewModel::class.java]

        quoteViewModel.quotes.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Loading -> {
                    Log.d("QuotesMVVM", "Response -> Loading...")
                    binding.lottieView.visibility = View.VISIBLE
                }

                is Response.Success -> {
                    binding.lottieView.visibility = View.GONE

                    it.data?.let { quotes ->
                        Log.d("QuotesMVVM", "Response -> Quotes Size: ${quotes.results.size}")
                        setupRecyclerView(quotes.results)
                    }
                }

                is Response.Error -> {
                    Log.e("QuotesMVVM", "Response -> Error: ${it.errorMessage}")
                    binding.lottieView.visibility = View.GONE
                }
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupRecyclerView(quotes: List<Result>) {
        val quotesAdapter = QuoteListAdapter(quotes)
        binding.rvQuotes.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = quotesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}