package com.gogulf.passenger.app.ui.schedulebooking.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityNewScheduledBookingBinding
import kotlinx.coroutines.launch

class NewScheduledBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewScheduledBookingBinding
    private lateinit var viewModel: ScheduledBookingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        SystemBarUtil.enableEdgeToEdge(this)
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityNewScheduledBookingBinding>(this, R.layout.activity_new_scheduled_booking).also {
            binding = it
            it.lifecycleOwner = this
            binding.activity = this
        }

        viewModel = ViewModelProvider(this)[ScheduledBookingViewModel::class.java]
        viewModel.customLoader = CustomLoader(this)
        binding.viewModel =viewModel

        viewModel.scheduledAdapter.observe(this) {
            binding.historyRecyclerView.adapter = it
        }

        viewModel.shouldShowEmptyStatement.observe(this) {
            if (it) {
                binding.historyRecyclerView.visibility = View.GONE
                binding.noDataLayout.root.visibility = View.VISIBLE
                binding.noData = "No Scheduled Bookings Found"
            } else {
                binding.noDataLayout.root.visibility = View.GONE
                binding.historyRecyclerView.visibility = View.VISIBLE

            }

        }
        lifecycleScope.launch {
            viewModel.uiState.collect {

                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }

                if (it.error != null) {
                    CustomAlertDialog(this@NewScheduledBookingActivity).setTitle(it.error.title)
                        .setMessage(it.error.message)
                        .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancellable(false).show()
                    viewModel.clearError()
                }

            }
        }

        addBackCB()

    }

    fun validate() {
        if (isTaskRoot) {
            startActivity(Intent(this@NewScheduledBookingActivity, GetARideActivityV2::class.java))
            finishAffinity()
            return
        }
        finish()
    }

    private fun addBackCB() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isTaskRoot) {
                    startActivity(Intent(this@NewScheduledBookingActivity, GetARideActivityV2::class.java))
                    finishAffinity()
                    return
                }
                finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback);
    }
}