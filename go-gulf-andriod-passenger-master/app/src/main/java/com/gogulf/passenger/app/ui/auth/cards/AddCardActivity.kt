package com.gogulf.passenger.app.ui.auth.cards

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.dashboard.DashboardActivity
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.MonthYearPicker
import com.gogulf.passenger.app.utils.enums.DefaultInputType
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.DateListener
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.google.gson.JsonObject
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityAddCardScreenBinding
import com.stripe.android.model.CardParams
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.HashMap

class AddCardActivity : BaseActivity<ActivityAddCardScreenBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityAddCardScreenBinding
    private var isDateSelected = false
    private val updateCardViewModel: AddCardsVM by viewModel()
    private var selectedDate = ""

    private val TAG = "AddCardActivity"
    private var title = ""
    private var addExtras = ""
    private var myCard: CardModels? = null
    //update information

    private var isEditingMode: Boolean = false
    private var editViewCardValue: String? = ""
    private var editViewYearValue: String? = ""
    private var editViewMonthsValue: String? = ""
    private var editViewHolderName: String? = ""
    private var editViewCVCValue: String? = ""
    private var cardId: Int? = null
    private var isCardActive: String? = ""


    private var returnValue = ""
    override fun getLayoutId(): Int = R.layout.activity_add_card_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityAddCardScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this

        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            title = bundle?.getString(IntentConstant.TITLE, "Add Card") ?: "Add Card"
            addExtras = bundle?.getString(IntentConstant.EXTRA, "") ?: ""
            myCard = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getSerializable(IntentConstant.SERIAL, CardModels::class.java)
            } else {
                bundle?.getSerializable(IntentConstant.SERIAL) as CardModels
            }

        } catch (e: Exception) {
            title = "Add Card"
            myCard = null
            DebugMode.e(TAG, e.message.toString(), "$TAG -> Card Catch")
        }

        isEditingMode = intent.getBooleanExtra("isEditing", false)


        if (isEditingMode) {
            editViewCardValue = intent.getStringExtra("editViewCardValue")
            editViewYearValue = intent.getStringExtra("editViewYearValue")
            editViewMonthsValue = intent.getStringExtra("editViewMonthsValue")
            editViewHolderName = intent.getStringExtra("editViewHolderName")
            editViewCVCValue = intent.getStringExtra("editViewCVCValue")
            cardId = intent.getIntExtra("cardId", 0)
            isCardActive = intent.getStringExtra("isCardActive")

            mViewDataBinding.cardNumber.text = editViewCardValue
            mViewDataBinding.cardExpiry.expiryField.text = "$editViewMonthsValue/$editViewYearValue"
            mViewDataBinding.cvc.text = editViewCVCValue
            mViewDataBinding.holdername.text = editViewHolderName

            if (isCardActive == "1") {
                mViewDataBinding.activeSwitch.isChecked = true
                mViewDataBinding.activeSwitch.isEnabled = false
                mViewDataBinding.cardActiveContainer.alpha = 0.6f
            } else {
                mViewDataBinding.activeSwitch.isChecked = false
            }
            selectedDate = "$editViewMonthsValue/$editViewYearValue"
            isDateSelected = true
            mViewDataBinding.cardNumber.isEnabled = false
            mViewDataBinding.cvc.isEnabled = false
            mViewDataBinding.holdername.isEnabled = false
            title = "Edit Card"
            mViewDataBinding.cardActiveContainer.visibility = View.VISIBLE
        } else {
            title = "Add Card"
            mViewDataBinding.cardActiveContainer.visibility = View.GONE
        }
        mViewDataBinding.title = title



        mViewDataBinding.cardNumber.setHintsEdit("Card Number")
        mViewDataBinding.holdername.setHintsEdit("Holder's Name")
        mViewDataBinding.cvc.setHintsEdit("CVC")
        mViewDataBinding.cardExpiry.textField.text = "Card Expiry"
        mViewDataBinding.cardExpiry.mainContainer.setOnClickListener {
            MonthYearPicker(
                this@AddCardActivity, object : DateListener {
                    override fun value(date: String) {
                        mViewDataBinding.cardExpiry.errorText.visibility = View.GONE
                        mViewDataBinding.cardExpiry.errorText.text = ""
                        isDateSelected = true
                        selectedDate = date
                        mViewDataBinding.cardExpiry.expiryField.text = date
                    }
                }, mViewDataBinding.cardExpiry.expiryField.text.toString()
            )
        }
        initValidations()
        cardAddedObserver()
        cardUpdateObserver()

        if (myCard != null) {
            dataMapping(myCard!!)
        }


    }

    private fun dataMapping(cards: CardModels) {
        mViewDataBinding.cvc.text = cards.cardVerificationNumber
        mViewDataBinding.cardNumber.text = cards.cardMasked
        val expireDate = cards.cardExpiryMonth + "/" + cards.cardExpiryYear
        mViewDataBinding.cardExpiry.expiryField.text = expireDate
        mViewDataBinding.holdername.text = cards.cardHolderName

        isDateSelected = true
        selectedDate = expireDate
        mViewDataBinding.cardNumber.setFocus(false)
        mViewDataBinding.cvc.setFocus(false)

    }

    fun initValidations() {
        mViewDataBinding.cvc.setInputType(DefaultInputType.PassNumber)
        mViewDataBinding.cvc.setMaxLength(4)
        mViewDataBinding.cardNumber.setMaxLength(19)
        mViewDataBinding.cardNumber.setInputType(DefaultInputType.Number)
        mViewDataBinding.holdername.setInputType(DefaultInputType.FullName)
    }

    private fun isValid(): Boolean {
        return if (mViewDataBinding.cardNumber.isValid("Card Number")) false
        else if (mViewDataBinding.holdername.isValid("Holder's Name")) {
            false
        } else if (!mViewDataBinding.holdername.isValidName) {
            mViewDataBinding.holdername.setErrorText("Invalid holder's name")
            false
        } else if (!isDateSelected) {
            mViewDataBinding.cardExpiry.errorText.visibility = View.VISIBLE
            mViewDataBinding.cardExpiry.errorText.text = "Card Expiry is required"
            false
        } else if (mViewDataBinding.cvc.isValid("CVC")) {
            mViewDataBinding.cardExpiry.errorText.visibility = View.GONE
            mViewDataBinding.cardExpiry.errorText.text = ""
            false
        } else true

    }

    private fun afterValidatedData(): HashMap<String, RequestBody> {
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap["card_number"] =
            RequestBody.create("text/plain".toMediaTypeOrNull(), mViewDataBinding.cardNumber.text)
        hashMap["card_holder_name"] =
            RequestBody.create("text/plain".toMediaTypeOrNull(), mViewDataBinding.holdername.text)
        hashMap["card_expiry"] = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedDate)
        hashMap["card_verification_code"] =
            RequestBody.create("text/plain".toMediaTypeOrNull(), mViewDataBinding.cvc.text)
        return hashMap
    }

    private fun afterValidatedDataJson(): JsonObject {
        val hashMap = JsonObject()
        hashMap.addProperty("card_number", mViewDataBinding.cardNumber.text)
        hashMap.addProperty("card_holder_name", mViewDataBinding.holdername.text)
        hashMap.addProperty("card_expiry", selectedDate)
        hashMap.addProperty("card_verification_code", mViewDataBinding.cvc.text)
        return hashMap
    }

    private fun createCardToken() {
        var cardMonths = "0"
        var cardYear = "0"
        if (selectedDate.split("/").size == 2) {
            cardMonths = selectedDate.split("/")[0]

            cardYear = selectedDate.split("/")[1]
        }
        updateCardViewModel.createCardToken(
            cardParams = CardParams(
                number = mViewDataBinding.cardNumber.text,
                expMonth = cardMonths.toInt(),
                expYear = cardYear.toInt(),
                cvc = mViewDataBinding.cvc.text,
                name = mViewDataBinding.holdername.text
            )
        )
    }


    private fun updateValidations(): JsonObject {
        val hashMap = JsonObject()
        hashMap.addProperty("id", myCard?.id)
        hashMap.addProperty("card_holder_name", mViewDataBinding.holdername.text)
        hashMap.addProperty("card_expiry", selectedDate)
        hashMap.addProperty("is_active", myCard?.isActive)
        return hashMap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }


    private fun checkValidations() {
        if (isValid()) {
            if (title == "Add Card") {
//                val requestModel = DefaultRequestModel()
//            requestModel.fileBody = afterValidatedData()
//                requestModel.body = afterValidatedDataJson()
                createCardToken()
//                updateCardViewModel.addCardFunction(requestModel)
            } else if (title == "Edit Card") {
//                val requestModel = DefaultRequestModel()
//                requestModel.body = editCardJsonBody()
                if (mViewDataBinding.activeSwitch.isChecked && isCardActive == "0") {
                    updateCardViewModel.updateCard(cardId)
                } else {
                    finish()
                }

//                updateCardViewModel.updateCardFunction(requestModel)
            } else {
                val requestModel = DefaultRequestModel()
                requestModel.body = updateValidations()
//                updateCardViewModel.updateCardFunction(requestModel)
            }
        }
    }

    override fun onValidated() {
        checkValidations()
//        onSubmit()
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
        gotoClass(DashboardActivity::class.java)
    }


    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this, cls, true)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this, cls, bundle, true)
    }


    private fun cardAddedObserver() {
        updateCardViewModel.addCardResponse.observe(this) {
            when (it.status) {
                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    errorAlertDialog(it.title, it.message)

                }

                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
                    CustomAlertDialog(this).setTitle(it.data?.title).setMessage(it.data?.message)
                        .setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                            finish()

                        }.setCancellable(false).show()
                }

                Status.LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }

    private fun cardUpdateObserver() {
        updateCardViewModel.updateCardResponse.observe(this) {
            when (it.status) {
                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    errorAlertDialog(it.title, it.message)

                }

                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
//                    val response = Gson().fromJson<BaseData<Card>>(
//                        it.data, object : TypeToken<BaseData<Card>>() {}.type
//                    )
                    CustomAlertDialog(this).setTitle(it.data?.title).setMessage(it.data?.message)
                        .setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }.setCancellable(false).show()

                }

                Status.LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }

    private fun editCardJsonBody(): JsonObject {
        val hashMap = JsonObject()
        hashMap.addProperty("id", cardId)
        hashMap.addProperty("card_holder_name", mViewDataBinding.holdername.text)
        hashMap.addProperty("card_expiry", selectedDate)
        if (mViewDataBinding.activeSwitch.isChecked) {
            hashMap.addProperty("is_active", 1)
        } else {
            hashMap.addProperty("is_active", 0)
        }

        return hashMap
    }


}