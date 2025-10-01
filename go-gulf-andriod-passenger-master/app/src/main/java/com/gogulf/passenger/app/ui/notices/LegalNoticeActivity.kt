package com.gogulf.passenger.app.ui.notices

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant.BUNDLE
import com.gogulf.passenger.app.utils.objects.IntentConstant.TITLE
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityLegalNoticeBinding
import kotlinx.coroutines.launch

class LegalNoticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLegalNoticeBinding
    private lateinit var viewModel: LegalNoticeViewModel


    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
        DataBindingUtil.setContentView<ActivityLegalNoticeBinding>(
            this, R.layout.activity_legal_notice
        ).let {
            binding = it
            it.lifecycleOwner = this
            viewModel = ViewModelProvider(
                this, LegalNoticeViewModelFactory(intent.getStringExtra("passing_policies"))
            )[LegalNoticeViewModel::class.java]
            viewModel.customLoader = CustomLoader(this@LegalNoticeActivity)
            it.viewModel = viewModel
        }


        try {
            val bundle = intent.getBundleExtra(BUNDLE)
            title = bundle?.getString(TITLE, "") ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            DebugMode.e("Intent", e.message.toString(), "Intent-> catch")
        }


        if(title == "Accept"){
            binding.acceptButton.visibility = View.VISIBLE
            binding.acceptButton.setOnClickListener {
                val intent = Intent(this@LegalNoticeActivity, GetARideActivityV2::class.java)
                startActivity(intent)
                finishAffinity()
            }

        }





        lifecycleScope.launch {
            viewModel.uiState.collect{
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvX.layoutManager = LinearLayoutManager(this)
        binding.rvX.adapter = viewModel.xAdapter.value

        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}