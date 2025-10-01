package com.gogulf.passenger.app.ui.invoices

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.InvoiceResponseData
import com.gogulf.passenger.app.ui.addextras.ChooseCardBottomSheet
import com.gogulf.passenger.app.ui.invoices.pdf.PdfViewActivity
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.customcomponents.PrimaryButton
import com.gogulf.passenger.app.BuildConfig
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityInvoiceBinding
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class InvoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInvoiceBinding
    private lateinit var viewModel: InvoiceViewModel
    private lateinit var timeBottomSheetDialog: BottomSheetDialog
    private lateinit var stripe: Stripe


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.setStatusBarColor(this)
        DataBindingUtil.setContentView<ActivityInvoiceBinding>(
            this@InvoiceActivity, R.layout.activity_invoice
        ).let {
            binding = it
            it.lifecycleOwner = this
            viewModel = ViewModelProvider(
                this, InvoiceViewModelFactory(
                    cacheDir
                )
            )[InvoiceViewModel::class.java]
            viewModel.customLoader = CustomLoader(this)
            it.viewModel = viewModel

        }

        stripe = Stripe(
            this, App.publishableKey
        )

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.recyclerInvoice.layoutManager = LinearLayoutManager(this)
        binding.recyclerInvoice.adapter = viewModel.invoiceAdapter.value

        viewModel.invoiceAdapter.value?.shouldMultipleClick?.observe(this) {
            if (it) {
            } else {

            }
            viewModel.invoiceAdapter.value?.notifyDataSetChanged()
        }

        binding.btnDownload.setOnClickListener {
            viewModel.selectedListId.clear()
            viewModel.invoiceAdapter.value?.shouldMultipleClick?.value =
                !viewModel.invoiceAdapter.value?.shouldMultipleClick?.value!!
        }

//        binding.btnDownloadAll.setOnClickListener {
//            if (viewModel.selectedListId.isNotEmpty()) {
//                val listOfFiles = GetTheFileData(viewModel.selectedListId)
//                viewModel.bulkDownloadFile(listOfFiles)
////                val intent = Intent(this@InvoiceActivity, PdfViewActivity::class.java)
////
////                intent.putExtra(PDFS, listOfFiles)
////                viewModel.invoiceAdapter.value?.shouldMultipleClick?.value =
////                    !viewModel.invoiceAdapter.value?.shouldMultipleClick?.value!!
////                startActivity(intent)
//            }
//        }


        timeBottomSheetDialog = BottomSheetDialog(this)
        val timeBottomSheetView = layoutInflater.inflate(R.layout.data_picker_bottom_sheet, null)

        timeBottomSheetDialog.dismissWithAnimation = true
        timeBottomSheetDialog.behavior.isHideable = true
        timeBottomSheetDialog.behavior.isDraggable = true
        SystemBarUtil.enableEdgeToEdge(timeBottomSheetDialog)

        val calendar = Calendar.getInstance()

        timeBottomSheetDialog.setCancelable(true)
        timeBottomSheetDialog.setContentView(timeBottomSheetView)
        val btnSelectDate = timeBottomSheetView.findViewById<PrimaryButton>(R.id.btnSelectDate)
        val sheetDatePicker = timeBottomSheetView.findViewById<DatePicker>(R.id.datePicker)
        val mainSheetLayout = timeBottomSheetView.findViewById<ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainSheetLayout) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars() + WindowInsetsCompat.Type.ime())
            v.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }
        sheetDatePicker.maxDate = calendar.timeInMillis



        binding.btnFromDate.setOnClickListener {
            timeBottomSheetView.findViewById<TextView>(R.id.tvTitle).text = "From"
            val calendar: Calendar = Calendar.getInstance()
            viewModel.startDateRange?.let { it1 -> calendar.setTimeInMillis(it1) }
            sheetDatePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            btnSelectDate.setOnClickListener {
                val day = sheetDatePicker.dayOfMonth
                val month = sheetDatePicker.month
                val year = sheetDatePicker.year
                val returnCalendar = Calendar.getInstance()
                returnCalendar[Calendar.YEAR] = year
                returnCalendar[Calendar.MONTH] = month
                returnCalendar[Calendar.DAY_OF_MONTH] = day
                returnCalendar.set(Calendar.HOUR_OF_DAY, 0)
                returnCalendar.set(Calendar.MINUTE, 0)
                returnCalendar.set(Calendar.SECOND, 0)
                returnCalendar.set(Calendar.MILLISECOND, 0)
                viewModel.startDateRange = returnCalendar.timeInMillis
                val dateFormatToView = SimpleDateFormat("dd MMM',' yyyy", Locale.getDefault())
                viewModel.updateStartDateString(dateFormatToView.format(returnCalendar.time))
                viewModel.setListForAdapter()
                timeBottomSheetDialog.dismiss()
            }
            timeBottomSheetDialog.show()
        }

        binding.btnToDate.setOnClickListener {
            timeBottomSheetView.findViewById<TextView>(R.id.tvTitle).text =
                "To"
            val calendar: Calendar = Calendar.getInstance()
            viewModel.endDateRange?.let { it1 -> calendar.setTimeInMillis(it1) }
            sheetDatePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            btnSelectDate.setOnClickListener {
                val day = sheetDatePicker.dayOfMonth
                val month = sheetDatePicker.month
                val year = sheetDatePicker.year
                val returnCalendar = Calendar.getInstance()
                returnCalendar[Calendar.YEAR] = year
                returnCalendar[Calendar.MONTH] = month
                returnCalendar[Calendar.DAY_OF_MONTH] = day
                viewModel.endDateRange = returnCalendar.timeInMillis
                val dateFormatToView = SimpleDateFormat("dd MMM',' yyyy", Locale.getDefault())
                viewModel.updateToDateString(dateFormatToView.format(returnCalendar.time))
                viewModel.setListForAdapter()
                timeBottomSheetDialog.dismiss()
            }
            timeBottomSheetDialog.show()
        }

        viewModel.invoiceAdapter.value?.setOnInvoiceClickListener(object :
            RecyclerInvoiceAdapter.OnInvoiceClickListener {
            override fun onInvoiceClick(invoice: InvoiceResponseData) {
                viewModel.currentInvoiceId = invoice.id

                if (invoice.status?.lowercase() == "need_authorization") {
                    viewModel.customLoader?.show()
                    val clientSecret = invoice.client_secret
                    val confirmParams = ConfirmPaymentIntentParams.create(
                        clientSecret!!
                    )
                    stripe.confirmPayment(this@InvoiceActivity, confirmParams)

                } else if (invoice.status?.lowercase() == "failed") {
                    val modal = ChooseCardBottomSheet(invoiceViewModel = viewModel)
                    supportFragmentManager.let { modal.show(it, ChooseCardBottomSheet.TAG) }
                } else {
                    val intent = Intent(this@InvoiceActivity, PdfViewActivity::class.java)
                    intent.putExtra(
                        "pdf",
                        "${BuildConfig.BASE_URL}${UrlName.INVOICE_PDF}/${invoice.id}"
                    )
                    intent.putExtra(
                        "invoice_id", invoice.id
                    )
                    startActivity(intent)
                }

            }

        })



        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }

                if (it.error != null) {

//                    ErrorDialog(this@InvoiceActivity, it.error).show()
                    viewModel.resetError()
                }



                if(!it.isLoading ){
                    if(it.invoiceResponseData.isNullOrEmpty()){
                        binding.noInvoiceCard.visibility = View.VISIBLE
                    }
                    else{
                        binding.noInvoiceCard.visibility = View.GONE
                    }
                }
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.myLoadedFile.observe(this) {
            it?.let {
                val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", it!!)

                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "application/pdf"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                val chooserIntent = Intent.createChooser(shareIntent, "Share File")
                viewModel.stopLoading()
                startActivity(chooserIntent)
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status

                if (status == StripeIntent.Status.Succeeded) {
                    viewModel.customLoader?.dismiss()

                    paymentIntent.id?.let { viewModel.hitInvoiceStatus(it, isPaid = true) }
                } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
                    viewModel.customLoader?.dismiss()

                    // Payment failed – allow the customer to retry the payment
                    paymentIntent.id?.let { viewModel.hitInvoiceStatus(it, isPaid = false) }

                    // Show error message to the user or handle the failure
                }
            }

            override fun onError(e: Exception) {
                // Handle the error
                viewModel.customLoader?.dismiss()

                viewModel.hitInvoiceStatus(isPaid = false)

                // Show error message to the user or handle the error
            }
        })
    }
}