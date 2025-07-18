package com.gsm.quotesmvvm

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
                    binding.loadingAnim.visibility = View.VISIBLE
                    binding.clNoNetworkContainer.visibility = View.GONE
                }

                is Response.Success -> {
                    binding.loadingAnim.visibility = View.GONE
                    binding.clNoNetworkContainer.visibility = View.GONE

                    it.data?.let { quotes ->
                        setupRecyclerView(quotes.results)
                    }
                }

                is Response.Error -> {
                    binding.loadingAnim.visibility = View.GONE
                    binding.clNoNetworkContainer.visibility = View.GONE
                }

                is Response.NoNetwork -> {
                    makeSpannableString()
                    binding.clNoNetworkContainer.visibility = View.VISIBLE
                    binding.loadingAnim.visibility = View.GONE
                }
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupRecyclerView(quotes: List<Result>) {
        val quotesAdapter = QuoteListAdapter(quotes, onItemClick = {
            val action = QuoteListFragmentDirections.actionNavigateToQuoteDetailsFragment(it)
            findNavController().navigate(action)
        })
        binding.rvQuotes.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = quotesAdapter
        }
    }

    private fun makeSpannableString() {
        val noNetworkStr =
            "No network! Please check your internet connection and\ntry again or load data from local server"

        val spannableString = SpannableString(noNetworkStr)
        // Define clickable span for "try again"
        val tryAgainStart = noNetworkStr.indexOf("try again")
        val tryAgainEnd = tryAgainStart + "try again".length

        val tryAgainSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Try fetching quotes again
                val randomNumber = (Math.random() * 10).toInt()
                quoteViewModel.retryFetchingQuotes(randomNumber)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }

        spannableString.setSpan(
            tryAgainSpan,
            tryAgainStart,
            tryAgainEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Define clickable span for "load data"
        val loadDataStart = noNetworkStr.indexOf("load data")
        val loadDataEnd = loadDataStart + "load data".length

        val loadDataSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Load quotes from DB
                quoteViewModel.loadQuotesFromDB()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }

        spannableString.setSpan(
            loadDataSpan,
            loadDataStart,
            loadDataEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvNoNetwork.apply {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}