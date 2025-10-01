package com.gogulf.passenger.app.ui.ratings

import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.google.gson.JsonObject
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.databinding.ActiviyRatingScreenBinding
import com.gogulf.passenger.app.utils.enums.DefaultInputType
import org.koin.android.viewmodel.ext.android.viewModel

class RatingActivity : BaseActivity<ActiviyRatingScreenBinding>() {

    private lateinit var mViewDataBinding: ActiviyRatingScreenBinding
    private val ratingVM: RatingVM by viewModel()
    private var bookingId = ""


    override fun getLayoutId(): Int = R.layout.activiy_rating_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActiviyRatingScreenBinding

        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            bookingId = bundle?.getString(IntentConstant.BOOKING_ID, "") ?: ""
        } catch (e: Exception) {
            log(e.message.toString(), "RatingActivity")
        }
        mViewDataBinding.doneButton.setOnClickListener { onDoneClicked() }
        mViewDataBinding.btnSkip.setOnClickListener { ratingVM.postSkip(bookingId) }

        ratingObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }


    private fun onDoneClicked() {
//        val objects = JsonObject()
//        objects.addProperty("booking_id", bookingId)
//        objects.addProperty("rating", mViewDataBinding.ratingBars.rating)
//        objects.addProperty("feedback", mViewDataBinding.textField.text.toString().trim())
//        ratingVM.postRating(objects)

        mViewDataBinding.tip.setInputType(DefaultInputType.Number)
        val objects = JsonObject()
        objects.addProperty("rating", mViewDataBinding.ratingBars.rating)
        objects.addProperty("review", mViewDataBinding.textField.text.toString().trim())
        objects.addProperty("tip", mViewDataBinding.tip.text.toString().trim())
        ratingVM.postRating(objects, bookingId)

    }

    private fun ratingObserver() {
        ratingVM.ratingResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showDialog()

                }
                Status.ERROR -> {
                    hideDialog()
                    errorAlertDialog(it.title, it.message)

                }
                Status.SUCCESS -> {
                    hideDialog()
                    CustomAlertDialog(this@RatingActivity).setTitle(it.data?.title)
                        .setMessage(it.data?.message).setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(this@RatingActivity, GetARideActivityV2::class.java)
                            intent.putExtra("shouldWait", true)
                            startActivity(intent)
                            finishAffinity()
                        }.setCancellable(false).show()


                }
            }
        }
    }


    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@RatingActivity, cls, true)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this@RatingActivity, cls, bundle, true)
    }
}