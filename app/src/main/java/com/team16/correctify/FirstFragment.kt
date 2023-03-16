package com.team16.correctify

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
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
    ): View? {

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

        submitButton.setOnClickListener {
            val prompt = promptTextView?.editText?.text.toString()
            viewModel.fixTextMistakes(prompt)
        }

        val resultText = view.findViewById<TextView>(R.id.result_text)
        viewModel.isLoading.observe(this.viewLifecycleOwner) { state ->
            if (!state) {
                Log.d("FinishedMistakes", "Response received: " + viewModel.lastResponse.value.toString())
                if (viewModel.lastResponse.value != null) {
                    resultText.text = viewModel.lastResponse.value.toString()
                }
            } else {
                Log.d("FinishedMistakes", "Request started")
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