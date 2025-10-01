package com.gogulf.passenger.app.ui.walkthrough

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.bumptech.glide.Glide
import com.gogulf.passenger.app.utils.PrefEntity
import com.gogulf.passenger.app.utils.Preferences
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.databinding.ActivityFirstWalkthroughScreenBinding

class WalkThrough1Activity : AppCompatActivity() {
    private lateinit var mViewDataBinding: ActivityFirstWalkthroughScreenBinding
    private val TAG = "WalkThrough1Activity"
    private var caseCounter = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        SystemBarUtil.enableEdgeToEdge(this)
        super.onCreate(savedInstanceState)
        mViewDataBinding = ActivityFirstWalkthroughScreenBinding.inflate(layoutInflater)
        setContentView(mViewDataBinding.root)
        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            caseCounter = bundle?.getInt(IntentConstant.ID, 0)!!
            changeImage()

        } catch (e: Exception) {
            caseCounter = 0
            changeImage()

            DebugMode.e(TAG, e.message.toString(), "Exceptions-> $caseCounter")
        }

        mViewDataBinding.bottomContainer.nextButton.setOnClickListener {
            if (caseCounter < 2) {
                val bundle = Bundle()
                bundle.putInt(IntentConstant.ID, caseCounter + 1)
                val intent = Intent(this, WalkThrough1Activity::class.java)
                intent.putExtra(IntentConstant.BUNDLE, bundle)
                startActivity(intent)
                finishAffinity()

            } else {
                Preferences.setPreference(this@WalkThrough1Activity, PrefEntity.FIRST_TIME, true)

                val intent = Intent(this, GetStartedActivity::class.java)
                startActivity(intent)
                finishAffinity()

            }
        }
    }

    private fun changeImage() {
        when (caseCounter) {
            0 -> {
                mViewDataBinding.topic = "Next"
                mViewDataBinding.title = "Secured Payment"
                mViewDataBinding.desc = "Keeping your information safe"
                mViewDataBinding.walkThroughImage.walkthroughIV.setImageResource(R.drawable.walkthrough1)

            }
            1 -> {
                mViewDataBinding.topic =
                    "Next"
                mViewDataBinding.title = "Your booking Your choice"
                mViewDataBinding.desc = "Instant and scheduled bookings"
                mViewDataBinding.walkThroughImage.walkthroughIV.setImageResource(R.drawable.walkthrough3)

            }
            2 -> {
                mViewDataBinding.topic =
                    "Finish"
                mViewDataBinding.title = "Competitive Fares"
                mViewDataBinding.desc = "with professional drivers"
                mViewDataBinding.walkThroughImage.walkthroughIV.setImageResource(R.drawable.walkthrough4)

            }
            3 -> {
                mViewDataBinding.topic =
                    "END"
                mViewDataBinding.title = "Reasonable Fare 2"
                mViewDataBinding.desc = "Competitive rates with professional drivers 2"
                Glide.with(this@WalkThrough1Activity).load(R.drawable.walkthrough4)
                    .into(mViewDataBinding.walkThroughImage.walkthroughIV)

            }
        }
    }

}