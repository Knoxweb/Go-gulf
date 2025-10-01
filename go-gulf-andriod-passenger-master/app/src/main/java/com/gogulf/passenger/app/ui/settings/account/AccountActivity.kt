package com.gogulf.passenger.app.ui.settings.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.settings.mycards.MyCardActivity
import com.gogulf.passenger.app.ui.settings.profile.EditProfileActivity
import com.gogulf.passenger.app.ui.settings.profile.EditProfileVM
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.SimpleClick
import com.gogulf.passenger.app.utils.others.CustomToast
import com.gogulf.passenger.app.data.model.ProfileResponseData
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.ui.auth.cards.AddCardActivity
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.databinding.ActivityAccountsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class AccountActivity : BaseActivity<ActivityAccountsBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityAccountsBinding
    private lateinit var adapter: AccountInformationAdapter
    private var accountInfoList = ArrayList<AccountInformationModel>()
    private val accountActViewModel: EditProfileVM by viewModel()

    private lateinit var userProfile: ProfileResponseData
    private var cardData: List<CardModels>? = null
    override fun getLayoutId(): Int = R.layout.activity_accounts

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityAccountsBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        mViewDataBinding.activity = this
        commonViewModel.navigator = this
        adapter = AccountInformationAdapter(accountInfoList)
        mViewDataBinding.accountInformationRecyclerView.hasFixedSize()
        mViewDataBinding.accountInformationRecyclerView.layoutManager = LinearLayoutManager(this)
        mViewDataBinding.accountInformationRecyclerView.adapter = adapter
        mViewDataBinding.cardNumberCardView.setOnClickListener {
            if (cardData.isNullOrEmpty()) {
                startActivity(Intent(this@AccountActivity, AddCardActivity::class.java))
            } else {
                startActivity(Intent(this@AccountActivity, MyCardActivity::class.java))
            }
        }

        adapter.onLayoutClicked(object : SimpleClick {
            override fun onClicked() {
                startActivity(Intent(this@AccountActivity, EditProfileActivity::class.java))
            }

        })
        profileObserver()
        cardObserver()
    }


    private fun cardObserver() {

        accountActViewModel.cardResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    cardData = it.data
                    mViewDataBinding.cardNumberCardView.visibility = View.VISIBLE
                    mViewDataBinding.cardLabelTextView.visibility = View.VISIBLE
                    if (cardData.isNullOrEmpty()) {
                        mViewDataBinding.cardHolderName.text = "Add card"
                        mViewDataBinding.cardNumber.text = ""
                    }
                    updateUI()
                    adapter.setLoading(false)

                    mViewDataBinding.cardNumberCardShimmer.visibility = View.GONE

                }

                Status.LOADING -> {
                    adapter.setLoading(true)
                }

                Status.ERROR -> {
                    adapter.setLoading(false)
                    progressDialog.dismissDialog()
                    CustomToast.show(mViewDataBinding.mainContainer, this, it.message)

                }
            }
        }
    }

    private fun profileObserver() {
        accountActViewModel.profileResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    userProfile = it.data!!
                    updateUI()
                    adapter.setLoading(false)

                    mViewDataBinding.cardNumberCardShimmer.visibility = View.GONE

                }

                Status.LOADING -> {
                    adapter.setLoading(true)
                }

                Status.ERROR -> {
                    adapter.setLoading(false)
                    progressDialog.dismissDialog()
                    CustomToast.show(mViewDataBinding.mainContainer, this, it.message)

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI() {
        addAccountInfo()
        adapter.notifyDataSetChanged()
        if (!cardData.isNullOrEmpty()) {
            val actualList: CardModels? = cardData?.first { it.is_active == true }
            if (actualList != null) {
                mViewDataBinding.cardHolderName.text = actualList.name
                mViewDataBinding.cardNumber.text = actualList.card_masked
            }
        }


    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@AccountActivity, cls, false)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this@AccountActivity, cls, bundle, false)
    }

    private fun addAccountInfo() {
        accountInfoList.clear()


        accountInfoList.add(
            AccountInformationModel(
                "Name", userProfile.name ?: ""
            )
        )
        accountInfoList.add(AccountInformationModel("Email", userProfile.email ?: ""))
        accountInfoList.add(AccountInformationModel("Phone", userProfile.mobile ?: ""))
    }

    private fun goToCards() {
        openNewActivity(this@AccountActivity, MyCardActivity::class.java, false)
    }

    override fun onResume() {
        super.onResume()
        accountActViewModel.getProfile()
    }

    override fun onValidated() {
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }
}