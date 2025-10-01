package com.gogulf.passenger.app.ui.auth.registerv2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gogulf.passenger.app.ui.notices.LegalNoticeActivity
import com.gogulf.passenger.app.ui.walkthrough.GetStartedActivity
import com.gogulf.passenger.app.utils.CommonUtils
import com.gogulf.passenger.app.utils.PreferencesAction
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityRegisterV2Binding
import kotlinx.coroutines.launch
import java.io.IOException

class RegisterActivityV2 : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterV2Binding
    private lateinit var viewModel: RegisterNewViewModel

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                assert(data != null)
                val uri = data!!.data
                viewModel.capturedProfilePicURI = uri
                try {
                    // Convert the image to Base64 using CommonUtils
                    val base64String =
                        uri?.let { CommonUtils.encodeImageToBase64(this@RegisterActivityV2, it) }
                    if (base64String != null) {
                        viewModel.profileImageBase64 = base64String
                    }
                    viewModel.path = CommonUtils.getRealPathFromURI(
                        viewModel.capturedProfilePicURI!!, this@RegisterActivityV2
                    )
                    binding.userPhoto.setImageURI(Uri.parse(viewModel.path!!))

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                // Handle error
            } else {
                // Handle other cases
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
        DataBindingUtil.setContentView<ActivityRegisterV2Binding>(
            this, R.layout.activity_register_v2
        ).also {
            binding = it
            binding.lifecycleOwner = this
            viewModel = ViewModelProvider(this)[RegisterNewViewModel::class.java]
            binding.viewModel = viewModel
            viewModel.customLoader = CustomLoader(this)

            binding.activity = this

        }


        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isUILoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }
                if (it.isFirstUpdateSuccess) {
                    ///////////////--------............

                    val bundle = Bundle()
                    bundle.putString(IntentConstant.TITLE, "Accept")
                    gotoClass(LegalNoticeActivity::class.java, bundle)
                }

                if (it.onLogoutSuccess) {
                    PreferencesAction.clearAll(this@RegisterActivityV2)
                    val intent = Intent(this@RegisterActivityV2, GetStartedActivity::class.java)
                    startActivity(intent)
                    this@RegisterActivityV2.finishAffinity()
                }
                if (it.error != null) {
                    CustomAlertDialog(this@RegisterActivityV2).setTitle(it.error.title)
                        .setMessage(it.error.message)
                        .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancellable(false).show()
                    viewModel.clearError()
                }
            }
        }

        binding.btnUploadPhoto.setOnClickListener {
            CustomAlertDialog(
                this
            ).setTitle("Choose From")
                .setMessage("Choose to capture a new photo or Select one from your gallery")
                .setPositiveText("Capture") { dialog, _ ->
                    ImagePicker.with(this@RegisterActivityV2).crop().maxResultSize(620, 620)
                        .cameraOnly().cropSquare().compress(512).galleryMimeTypes(
                            mimeTypes = arrayOf(
                                "image/png", "image/jpg", "image/jpeg"
                            )
                        ).createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }.setNegativeText("Gallery") { dialog, _ ->
                    ImagePicker.with(this@RegisterActivityV2).crop().maxResultSize(620, 620)
                        .galleryOnly().cropSquare().compress(512).galleryMimeTypes(
                            mimeTypes = arrayOf(
                                "image/png", "image/jpg", "image/jpeg"
                            )
                        ).createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }.show()
        }


        binding.etFullName.getEditText().imeOptions = EditorInfo.IME_ACTION_NEXT
        binding.etEmail.getEditText().imeOptions = EditorInfo.IME_ACTION_DONE
        binding.etEmail.getEditText()
            .setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    p0: TextView?, actionId: Int, keyEvent: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        viewModel.hitFirstUpdate()
                        return true
                    }
                    return false
                }

            })



    }


    private fun gotoClass(cls: Class<*>, bundle: Bundle, finish: Boolean = false) {
        openNewActivity(this@RegisterActivityV2, cls, bundle, finish)
    }

    fun openNewActivity(
        activity: AppCompatActivity,
        cls: Class<*>,
        bundle: Bundle,
        finishCurrent: Boolean
    ) {
        val intent = Intent(activity, cls)
        intent.putExtra(IntentConstant.BUNDLE, bundle)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        if (finishCurrent) activity.finish()
    }
}