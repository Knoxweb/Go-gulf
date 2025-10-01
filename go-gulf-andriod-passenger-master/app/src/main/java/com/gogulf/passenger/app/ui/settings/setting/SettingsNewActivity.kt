package com.gogulf.passenger.app.ui.settings.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.data.apidata.FirebaseConstant.TYPE_PRIVACY_POLICY
import com.gogulf.passenger.app.data.apidata.FirebaseConstant.TYPE_TERMS_OF_USE
import com.gogulf.passenger.app.ui.getaridev2.RecyclerMenuAdapter
import com.gogulf.passenger.app.ui.notices.LegalNoticeActivity
import com.gogulf.passenger.app.ui.settings.account.AccountActivity
import com.gogulf.passenger.app.ui.walkthrough.GetStartedActivity
import com.gogulf.passenger.app.utils.PreferencesAction
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivitySettingsNewBinding
import kotlinx.coroutines.launch

class SettingsNewActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsNewBinding
    private lateinit var viewModel: SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        SystemBarUtil.enableEdgeToEdge(this)
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySettingsNewBinding>(
            this, R.layout.activity_settings_new
        ).also {
            binding = it
            it.lifecycleOwner = this
            binding.activity = this
            viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
            binding.viewModel = viewModel
            viewModel.customLoader = CustomLoader(this@SettingsNewActivity)
        }

        viewModel.menuAdapter.value?.setOnMenuClickListener(object :
            RecyclerMenuAdapter.onMenuClicked {
            override fun onMenuClicked(position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(this@SettingsNewActivity, AccountActivity::class.java)
                        startActivity(intent)
                    }

                    1 -> {

                        val intent =
                            Intent(this@SettingsNewActivity, LegalNoticeActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString(IntentConstant.TITLE, "Privacy Policy")
                        intent.putExtra(IntentConstant.BUNDLE, bundle)
                        intent.putExtra("passing_policies", TYPE_PRIVACY_POLICY)
                        startActivity(intent)
                    }

                    2 -> {
                        val intent =
                            Intent(this@SettingsNewActivity, LegalNoticeActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString(IntentConstant.TITLE, "Terms & Conditions")
                        intent.putExtra(IntentConstant.BUNDLE, bundle)
                        intent.putExtra("passing_policies", TYPE_TERMS_OF_USE)
                        startActivity(intent)
                    }
                }
            }
        })
        binding.recyclerMenuItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerMenuItems.adapter = viewModel.menuAdapter.value

        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.hide()
                }
                if (it.error != null) {
                    CustomAlertDialog(this@SettingsNewActivity).setTitle(it.error.title).setMessage(it.error.message)
                        .setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.setCancellable(false).show()
                    viewModel.resetError()
                }
                if (it.onLogoutSuccess) {
                    PreferencesAction.clearAll(this@SettingsNewActivity)
                    val intent = Intent(this@SettingsNewActivity, GetStartedActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            CustomAlertDialog(this@SettingsNewActivity).setTitle("Logout?")
                .setMessage("Are you sure you want to logout?")
                .setPositiveText("Yes") { dialog, _ ->
                    dialog.dismiss()
                    viewModel.hitLogout()
                }.setNegativeText("No") { dialog, _ -> dialog.dismiss() }.setCancellable(true)
                .show()
        }


        binding.btnDeleteAccount.setOnClickListener {
            CustomAlertDialog(this@SettingsNewActivity).setTitle("Delete account?")
                .setMessage("Are you sure you want to delete account?\nAll of the documents and information will get deleted. After deletion, you cannot login with this account.")
                .setPositiveText("Yes") { dialog, _ ->
                    dialog.dismiss()
                    viewModel.hitDeleteAccount()
                }.setNegativeText("No") { dialog, _ -> dialog.dismiss() }.setCancellable(true)
                .show()
        }


    }
}