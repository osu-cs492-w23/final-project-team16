package com.team16.correctify.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        val submitButton = view.findViewById<Button>(R.id.submit_button)
        val clearButton = view.findViewById<Button>(R.id.clear_button)
        val promptTextView = view.findViewById<TextInputLayout>(R.id.input_text_layout)
        val resultText = view.findViewById<TextView>(R.id.result_text)
        val loadingIndicator: CircularProgressIndicator = view.findViewById(R.id.loading_indicator)

        clearButton.isEnabled = false

        submitButton.setOnClickListener {
            val prompt = promptTextView?.editText?.text.toString()
            if (prompt.isNotEmpty()) {
                viewModel.fixTextMistakes(prompt)
            } else {
                resultText.text = getString(R.string.empty_prompt)
            }
        }

        clearButton.setOnClickListener {
            promptTextView.editText?.text?.clear()
            resultText.text = ""
        }

        // disable clear button while there is text in the prompt
        promptTextView.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                clearButton.isEnabled = s?.isNotEmpty()!!
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        viewModel.loadingStatus.observe(this.viewLifecycleOwner) { uiState ->
            when (uiState) {
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                    submitButton.isEnabled = false
                }
                LoadingStatus.SUCCESS -> {
                    Log.d(
                        "FinishedMistakes",
                        "Response received: " + viewModel.lastResponse.value.toString()
                    )

                    loadingIndicator.visibility = View.INVISIBLE
                    submitButton.isEnabled = true

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