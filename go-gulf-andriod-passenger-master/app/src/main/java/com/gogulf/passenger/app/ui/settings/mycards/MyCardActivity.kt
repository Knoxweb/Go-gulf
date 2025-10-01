package com.gogulf.passenger.app.ui.settings.mycards

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.ui.auth.cards.AddCardActivity
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.CardUpdateListener
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.google.gson.JsonObject
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.databinding.ActivityMyCardScreenBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MyCardActivity : BaseActivity<ActivityMyCardScreenBinding>(), BaseNavigation {

    private lateinit var mViewDataBinding: ActivityMyCardScreenBinding
    private val myCardVM: MyCardVM by viewModel()
    private lateinit var adapter: CardListAdapter
    private var myCardList = ArrayList<CardModels>()
    private lateinit var myActiveCard: CardModels
    private val TAG = "MyCardActivity"


    override fun getLayoutId(): Int = R.layout.activity_my_card_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityMyCardScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        mViewDataBinding.activity = this
        commonViewModel.navigator = this

        cardObserver()

        adapter = CardListAdapter(this@MyCardActivity, myCardList, object : CardUpdateListener {
            override fun update(card: CardModels) {
//                gotoUpdateCard(card)
                myCardVM.deleteCard(card.id)

            }
        })
        mViewDataBinding.cardRecyclerViews.apply {
            layoutManager = LinearLayoutManager(this@MyCardActivity)
        }
        mViewDataBinding.cardRecyclerViews.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
//        myCardVM.getCardsInformation()
    }

    override fun onValidated() {
        gotoAddCard()
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }

    private fun gotoAddCard() {
        openNewActivity(this@MyCardActivity, AddCardActivity::class.java, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }

    private fun gotoUpdateCard(cardModel: CardModels) {
        val bundle = Bundle()
        bundle.putString(IntentConstant.TITLE, "Update Card")
        bundle.putSerializable(IntentConstant.SERIAL, cardModel)
        openNewActivity(this@MyCardActivity, AddCardActivity::class.java, bundle, false)
    }

    private fun setActiveCard(myList: ArrayList<CardModels>) {
        for (card in myList) {
            if (card.is_active == true) {
                myActiveCard = card
                break
            }
        }
        if (myList.size > 0) mViewDataBinding.cardModel = myActiveCard
    }

    private fun deleteCardJsonObject(cardId: Int?): JsonObject {
        val hashMap = JsonObject()
        hashMap.addProperty("id", cardId)
        return hashMap
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cardObserver() {
        myCardVM.cardResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.show()

                }

                Status.SUCCESS -> {
                    progressDialog.dismissDialog()

                    myCardList.clear()
                    it.data?.let { it1 -> myCardList.addAll(it1) }

                    adapter.notifyDataSetChanged()
                    setActiveCard(myCardList)
                }

                Status.ERROR -> {
                    progressDialog.dismissDialog()
                }
            }
        }
    }
}