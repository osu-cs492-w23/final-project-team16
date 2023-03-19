package com.team16.correctify.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.team16.correctify.R
import com.team16.correctify.data.LoadingStatus
import com.team16.correctify.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val viewModel: OpenAPIViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Uncomment to run test as view is created
        /*if (!viewModel.isLoading.value!!) {
            viewModel.fixTextMistakes("Wat day of the wek is it")
        }*/

        // bind to submit button
        val submitButton = view.findViewById<Button>(R.id.submit_button)
        val promptTextView = view.findViewById<TextInputLayout>(R.id.input_text_layout)
        val resultText = view.findViewById<TextView>(R.id.result_text)
        val loadingIndicator: CircularProgressIndicator = view.findViewById(R.id.loading_indicator)

        submitButton.setOnClickListener {
            val prompt = promptTextView?.editText?.text.toString()
            if (prompt.isNotEmpty()) {
                viewModel.fixTextMistakes(prompt)
            } else {
                resultText.text = "Please enter a prompt"
            }
        }

        viewModel.loadingStatus.observe(this.viewLifecycleOwner) { uiState ->
            when (uiState) {
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                }
                LoadingStatus.SUCCESS -> {
                    Log.d(
                        "FinishedMistakes",
                        "Response received: " + viewModel.lastResponse.value.toString()
                    )

                    loadingIndicator.visibility = View.INVISIBLE

                    if (viewModel.lastResponse.value != null) {
                        resultText.text = viewModel.lastResponse.value.toString()
                    }
                }
                else -> {
                    Log.d("FinishedMistakes", "Request failed")
                }
            }
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}