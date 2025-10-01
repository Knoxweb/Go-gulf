package com.gogulf.passenger.app.ui.auth.login

import android.app.Dialog
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.gogulf.passenger.app.ui.auth.passengerregister.PassengerRegisterViewModel
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.SelectCountryBottomSheetBinding

class CountrySelectBottomSheet(
    private val viewModel: LoginViewModel? = null,
    private val passengerRegisterViewModel: PassengerRegisterViewModel? = null,

    ) : BottomSheetDialogFragment() {

    private lateinit var binding: SelectCountryBottomSheetBinding

    private var isShowBottom = MutableLiveData<Boolean>().apply {
        value = false
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = SelectCountryBottomSheetBinding.inflate(inflater, container, false)
        binding.recyclerChooseCountry.layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration =
            MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)
        itemDecoration.dividerColor = resources.getColor(R.color.editTextBackground)
        binding.recyclerChooseCountry.addItemDecoration(
            itemDecoration
        )
        binding.recyclerChooseCountry.adapter = RecyclerChooseCountryAdapter(
            countryList,
            object : RecyclerChooseCountryAdapter.ChooseCountryListener {
                override fun onChooseCountry(countryModel: CountryModel) {
                    viewModel?.updateCountrySelected(countryModel)
                    passengerRegisterViewModel?.updateCountrySelected(countryModel)

                    isShowBottom.value = true
                }

            })
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerChooseCountry) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val dp24 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics
            ).toInt()
            v.setPadding(0, 0, 0, systemBars.bottom + dp24)
            insets
        }


        isShowBottom.observe(viewLifecycleOwner) {
            if (it) {
                dialog?.dismiss()
            }
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }
}