package com.gsm.quotesmvvm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gsm.quotesmvvm.databinding.FragmentQuoteDetailsBinding

class QuoteDetailsFragment : Fragment() {

    private var _binding: FragmentQuoteDetailsBinding? = null
    private val binding get() = _binding!!
    val args: QuoteDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuoteDetailsBinding.inflate(inflater, container, false)

        val quote = args.quoteItem
        Log.d("QuoteDetailsFragment", "QuoteItem: $quote")

        binding.apply {
            tvQuote.text = quote.content
            tvAuthor.text = quote.author
            tvDateAdded.text = quote.dateAdded

            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            fabShare.setOnClickListener {
                val shareText = "\"${quote.content}\"\n\n- ${quote.author}"

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }

                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}