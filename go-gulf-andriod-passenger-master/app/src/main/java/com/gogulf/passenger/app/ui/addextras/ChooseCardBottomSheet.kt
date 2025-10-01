package com.gogulf.passenger.app.ui.addextras

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.ui.auth.cards.AddCardActivity
import com.gogulf.passenger.app.ui.invoices.InvoiceViewModel
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ChooseCardBottomSheetBinding

class ChooseCardBottomSheet(
    private val bookingViewModel: AddExtrasVM? = null,
    private val invoiceViewModel: InvoiceViewModel? = null
) : BottomSheetDialogFragment() {

    private lateinit var binding: ChooseCardBottomSheetBinding

    private var isShowBottom = MutableLiveData<Boolean>().apply {
        value = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        SystemBarUtil.enableEdgeToEdge(dialog)
        DataBindingUtil.inflate<ChooseCardBottomSheetBinding>(
            inflater, R.layout.choose_card_bottom_sheet, container, false
        ).also {
            binding = it
            it.lifecycleOwner = this
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        bookingViewModel?.chooseCardAdapter?.value?.setCardClickListener(object :
            RecyclerChoosePaymentAdapter.OnCardClickListener {
            override fun onCardClick(card: CardModels) {
                dismiss()
            }

        })



            binding.recycler.adapter = bookingViewModel?.chooseCardAdapter?.value

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars() + WindowInsetsCompat.Type.ime())
            v.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }


        binding.btnAddPayment.setOnClickListener {
//            val bottomSheetFragment = PaymentCardBottomSheet(
//                viewModel = AddPaymentViewModel(),
//                confirmBookingViewModel = bookingViewModel,
//                invoiceViewModel = invoiceViewModel
//            )
//            bottomSheetFragment.show(
//                requireActivity().supportFragmentManager, bottomSheetFragment.tag
//            )
            val bundle = Bundle()
            bundle.putString(IntentConstant.EXTRA, "extra")
            val intent = Intent(requireContext(), AddCardActivity::class.java)
            intent.putExtra(IntentConstant.BUNDLE, bundle)
            startActivity(intent)
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