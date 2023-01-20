package com.techventure.salbiapp.currency.views.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.techventure.androidext.ext.beGone
import com.techventure.androidext.ext.beVisible
import com.techventure.androidext.ext.showToast
import com.techventure.salbiapp.audioapp.views.activities.AudioActivity
import com.techventure.salbiapp.currency.viewmodel.CurrencyViewModel
import com.techventure.salbiapp.databinding.FragmentCurrencyBinding
import com.techventure.salbiapp.views.baseviews.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_currency.*
import kotlinx.coroutines.flow.collect

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CurrencyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CurrencyFragment : BaseFragment() {

    private lateinit var currencyBinding: FragmentCurrencyBinding
    private val viewModel: CurrencyViewModel by viewModels()

    private var param1: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1) ?: "Argument not fount"
            context?.showToast(param1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currencyBinding = FragmentCurrencyBinding.inflate(inflater, container, false)
        return currencyBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startActivity(Intent(requireContext(), AudioActivity::class.java))
        currencyBinding.btnConvert.setOnClickListener {
            viewModel.convert(
                etFrom.text.toString(),
                spFromCurrency.selectedItem.toString(),
                spToCurrency.selectedItem.toString()
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is CurrencyViewModel.CurrencyEvent.Success -> {
                        currencyBinding.apply {
                            progressBar.beGone()
                            tvResult.setTextColor(Color.BLACK)
                            tvResult.text = event.resultText
                        }
                    }
                    is CurrencyViewModel.CurrencyEvent.Error -> {
                        currencyBinding.apply {
                            progressBar.beGone()
                            tvResult.setTextColor(Color.RED)
                            tvResult.text = event.errorText
                        }
                    }
                    is CurrencyViewModel.CurrencyEvent.Loading -> {
                        currencyBinding.progressBar.beVisible()
                    }
                    else -> Unit
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CurrencyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param: String) =
            CurrencyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param)
                }
            }
    }
}