package com.gogulf.passenger.app.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.dashboards.ProfileModel
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.currentride.CurrentRideActivity
import com.gogulf.passenger.app.ui.settings.profile.EditProfileActivity
import com.gogulf.passenger.app.ui.shortcuts.ShortcutAddModel
import com.gogulf.passenger.app.ui.shortcuts.ShortcutsActivity
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.DashboardShortcutListener
import com.gogulf.passenger.app.utils.objects.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import com.gogulf.passenger.app.ui.currentridenew.NewCurrentRideActivity
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.utils.CommonUtils
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.imageFromGlide
import com.gogulf.passenger.app.databinding.ActivityDashboardScreenBinding
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException

class DashboardActivity : BaseActivity<ActivityDashboardScreenBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityDashboardScreenBinding
    private val TAG = "DashboardActivity"
    private val dashBoardVM: DashboardVM by viewModel()


    private lateinit var myBitmap: Bitmap
    private lateinit var picUri: Uri

    private var mShortcutList = ArrayList<ShortcutAddModel>()
    private lateinit var mShortcutAdapter: DashboardShortcutAdapter

    override fun getLayoutId(): Int = R.layout.activity_dashboard_screen


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                assert(data != null)
                val uri = data!!.data
                dashBoardVM.capturedProfilePicURI = uri
                try {
                    // Convert the image to Base64 using CommonUtils
                    val base64String = uri?.let { CommonUtils.encodeImageToBase64(this, it) }
                    if (base64String != null) {
                        dashBoardVM.profileImageBase64 = base64String
                    }
                    dashBoardVM.path = CommonUtils.getRealPathFromURI(
                        dashBoardVM.capturedProfilePicURI!!, this
                    )
                    dashBoardVM.hitFirstUpdate()
//                    binding.userImageView.setImageURI(Uri.parse(viewModel.path!!))

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else if (resultCode == com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR) {
                // Handle error
            } else {
                // Handle other cases
            }
        }


    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityDashboardScreenBinding


        mViewDataBinding.viewModel = dashBoardVM
        dashBoardVM.customLoader = CustomLoader(this@DashboardActivity)
        mViewDataBinding.lifecycleOwner = this

        mViewDataBinding.backButton.setOnClickListener {
//            gotoClass(MenuActivity::class.java)
            finish()
        }
        mViewDataBinding.addLayoutContainer.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ShortcutsActivity::class.java)
            startActivity(intent)
//            gotoClass(ShortcutsActivity::class.java)
        }
//        mViewDataBinding.userImageView.setOnClickListener {
//            checkPhotoPermission()
//        }
        mViewDataBinding.editProfileTV.setOnClickListener {
//            gotoClass(EditProfileActivity::class.java)
            val intent = Intent(this@DashboardActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }
        mViewDataBinding.bookNowContainer.setOnClickListener {
//            gotoClass(GetARideActivity::class.java)
            val intent = Intent(this@DashboardActivity, GetARideActivityV2::class.java)
            startActivity(intent)
            finishAffinity()
        }



        mViewDataBinding.cardImageView.setOnClickListener {
            CustomAlertDialog(
                this
            ).setTitle("Choose From")
                .setMessage("Choose to capture a new photo or Select one from your gallery")
                .setPositiveText("Capture") { dialog, _ ->
                    com.github.dhaval2404.imagepicker.ImagePicker.with(this).crop()
                        .maxResultSize(620, 620).cameraOnly().cropSquare().compress(512)
                        .galleryMimeTypes(
                            mimeTypes = arrayOf(
                                "image/png", "image/jpg", "image/jpeg"
                            )
                        ).createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }.setNegativeText("Gallery") { dialog, _ ->
                    com.github.dhaval2404.imagepicker.ImagePicker.with(this).crop()
                        .maxResultSize(620, 620).galleryOnly().cropSquare().compress(512)
                        .galleryMimeTypes(
                            mimeTypes = arrayOf(
                                "image/png", "image/jpg", "image/jpeg"
                            )
                        ).createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }.show()

        }


        mShortcutAdapter = DashboardShortcutAdapter(
            this@DashboardActivity,
            mShortcutList,
            object : DashboardShortcutListener {
                override fun editShortcut(shortcut: ShortcutAddModel) {
                    val bundle = Bundle()
                    bundle.putSerializable(IntentConstant.SERIAL, shortcut)
                    if (shortcut.id != null) {
                        bundle.putInt(IntentConstant.ID, shortcut.id)
                        val intent = Intent(this@DashboardActivity, ShortcutsActivity::class.java)
                        intent.putExtra(IntentConstant.BUNDLE, bundle)
                        startActivity(intent)
                    }
                }

                override fun deleteShortcut(shortcut: ShortcutAddModel) {
                    dashBoardVM.deleteShortcut(shortcut.id)
                }
            })

        mViewDataBinding.shortcutRecyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@DashboardActivity)
        }
        mViewDataBinding.shortcutRecyclerView.adapter = mShortcutAdapter
        var updateIcon = false

        mShortcutAdapter.updateIcon(updateIcon)
        mViewDataBinding.manageFav.setOnClickListener {
            updateIcon = !updateIcon
            mShortcutAdapter.updateIcon(updateIcon)
        }
        getProfileUpdateObserver()
    }

    private fun checkPhotoPermission() {
        if (!PermissionCheckApp.checkPermissionMedia(this@DashboardActivity)) {
            PermissionCheckApp.requestPermissionCamera(this@DashboardActivity)
        } else {
            activityResult.launch(ImagePicker.getPickImageChooserIntent(this@DashboardActivity))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
        dashBoardVM.shortcutLiveDataList.observe(this) {
            mShortcutList.clear()
            mShortcutList.addAll(it)
            mShortcutAdapter.notifyDataSetChanged()
        }


        lifecycleScope.launch {
            dashBoardVM.loadingUiState.collect {
                if (it.isLoading) {
                    dashBoardVM.customLoader?.show()
                } else {
                    dashBoardVM.customLoader?.dismiss()
                }
                if (it.error != null) {
                    CustomAlertDialog(this@DashboardActivity).setTitle(it.error.title)
                        .setMessage(it.error.message)
                        .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancellable(false).show()
                    dashBoardVM.clearError()
                }
            }
        }


        mViewDataBinding.current.setOnClickListener {
            startActivity(Intent(this, NewCurrentRideActivity::class.java))
            finishAffinity()
        }

        dashBoardVM.currentRideResponseData.observe(this@DashboardActivity) {
            if (it != null) {
                mViewDataBinding.current.visibility = View.VISIBLE
                mViewDataBinding.bookNowContainer.visibility = View.GONE
                mViewDataBinding.fleetImageCurrent.imageFromGlide(it.fleet?.image_url)
//                binding.tvClassCurrent.text = it.fleet?.name
                mViewDataBinding.tvScheduledDateCurrent.text = it.pickup_date_time
                mViewDataBinding.tvAirportInfoCurrent.text = it.drop?.name
                mViewDataBinding.tvPickUpCurrent.text = "Pickup at " + it.pickup?.name
                mViewDataBinding.tvPriceCurrent.text = "$" + it.fare
            } else {
                mViewDataBinding.current.visibility = View.GONE
                mViewDataBinding.bookNowContainer.visibility = View.VISIBLE

            }
        }
    }

    override fun onResume() {
        super.onResume()
//        realTimeDatabase()
    }

    override fun onStart() {
        super.onStart()

//        getShortcuts()
    }

    override fun onDestroy() {
        super.onDestroy()
        shortCutListener?.remove()
    }

    override fun onValidated() {
    }

    override fun onBackPress() {
        CustomAlertDialog(this@DashboardActivity).setTitle("Exit App")
            .setMessage("Are you sure you want to exit app?").setPositiveText("Yes") { dialog, _ ->
                dialog.dismiss()
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
            }.setNegativeText("No") { dialog, _ -> dialog.dismiss() }.setCancellable(false).show()
    }

    override fun onBackPressed() {
        onBackPress()
    }

    override fun onSubmit() {

    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this, cls, false)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this, cls, bundle, false)
    }

    private var encodedImage = ""

    private var activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            var bitmap: Bitmap? = null
            Log.e(TAG, "on activity Result ${result!!}")
            if (result.resultCode == Activity.RESULT_OK) {

                if (ImagePicker.getPickImageResultUri(
                        this@DashboardActivity, result.data
                    ) != null
                ) {
                    picUri =
                        ImagePicker.getPickImageResultUri(this@DashboardActivity, result.data)!!

                    try {
                        myBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, picUri)
                        myBitmap = ImagePicker.getResizedBitmap(myBitmap, 500)

                        mViewDataBinding.userImageView.setImageBitmap(myBitmap)
                        try {
                            encodedImage = ImagePicker.encodeImage(myBitmap)

                        } catch (e: Exception) {
                            e.printStackTrace();
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {

                    val photo = result.data?.extras?.get("data") as Bitmap
                    mViewDataBinding.userImageView.setImageBitmap(photo)
//                    picUri = getImageUri(this, photo!!)!!
                    try {
                        encodedImage = ImagePicker.encodeImage(photo)
                    } catch (e: Exception) {
                        e.printStackTrace();
                    }

                }
                val body = JsonObject()
                body.addProperty("image", "${Constants.IMAGE_64}$encodedImage")
                dashBoardVM.updateProfilePersonal(body)

            }
        }


    private fun getProfileUpdateObserver() {
        dashBoardVM.profileUpdateResponse.observe(this) {
            when (it.status) {
                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    errorAlertDialog(it.title, it.message)
                }

                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
                    val response = GetResponseModel.toObject<ProfileModel>(it.data)


                }

                Status.LOADING -> {
//                    progressDialog.show()

                }
            }
        }
    }


//    private fun dashBoardContent() {
//
//        dashBoardVM.dashResponse.observe(this@DashboardActivity) {
//            when (it.status) {
//                Status.ERROR -> {
//                    mViewDataBinding.nameShimmer.visibility = View.GONE
//                    mViewDataBinding.userNameTV.visibility = View.VISIBLE
//                    mViewDataBinding.userNameTV.text =
//                        preferenceHelper.getValue(PrefConstant.FULL_NAME, "") as String
//
//                }
//
//                Status.SUCCESS -> {
//                    mViewDataBinding.nameShimmer.visibility = View.GONE
//                    mViewDataBinding.userNameTV.visibility = View.VISIBLE
//
//                    val response = GetResponseModel.toObject<DashboardModel>(it.data)
//                    Glide.with(this@DashboardActivity).load(response.data.user.imageLink)
//                        .placeholder(R.drawable.default_no_image)
//                        .into(mViewDataBinding.userImageView)
//                    mViewDataBinding.userNameTV.text = response.data.user.name
//                    preferenceHelper.setValue(PrefConstant.FULL_NAME, response.data.user.name)
//                    App.passengerName.postValue(response.data.user.name)
//                    mViewDataBinding.totalBookingContainer.booking = response.data.completedBooking
//                    mViewDataBinding.totalBookingContainer.time = response.data.nextBooking
//
//                    if (response.data.isCardAdded == "0") {
//                        CustomAlertDialog(this@DashboardActivity).setTitle("Add Card")
//                            .setMessage("Please add card").setPositiveText("NOW") { dialog, _ ->
//                                dialog.dismiss()
//                                val bundle = Bundle()
//                                bundle.putString(IntentConstant.TITLE, "Add Card")
//                                gotoClass(AddCardActivity::class.java, bundle)
//
//
//                            }.setNegativeText("LATER") { dialog, _ ->
//                                dialog.dismiss()
//                            }.show()
//                    }
//
//
//                }
//
//                Status.LOADING -> {
//                    mViewDataBinding.nameShimmer.visibility = View.VISIBLE
//                    mViewDataBinding.userNameTV.visibility = View.GONE
//                }
//            }
//        }
//
//
//    }

    /*    private fun stateFlowObserver() {

            lifecycleScope.launchWhenCreated {
                dashBoardVM.uiState.collect {
                    when (it.status) {
                        Status.ERROR -> {
                            mViewDataBinding.nameShimmer.visibility = View.GONE
                            mViewDataBinding.userNameTV.visibility = View.VISIBLE

                        }
                        Status.SUCCESS -> {
                            mViewDataBinding.nameShimmer.visibility = View.GONE
                            mViewDataBinding.userNameTV.visibility = View.VISIBLE

                            val response = Gson().fromJson<BaseData<DashboardModel>>(
                                it.data,
                                object : TypeToken<BaseData<DashboardModel>>() {}.type
                            )
    //                    binding.homeViewModel = response.data
                            Glide.with(this@DashboardActivity).load(response.data.user.imageLink)
                                .placeholder(R.drawable.default_no_image)
                                .into(mViewDataBinding.userImageView)
                            mViewDataBinding.userNameTV.text = response.data.user.name
                            mViewDataBinding.totalBookingContainer.booking =
                                response.data.completedBooking
                            mViewDataBinding.totalBookingContainer.time =
                                response.data.nextBooking

                            if (response.data.isCardAdded == "0") {
                                CustomAlertDialog(this@DashboardActivity).setTitle("Add Card")
                                    .setMessage("Please add card")
                                    .setPositiveText("NOW") { dialog, _ ->
                                        dialog.dismiss()
                                        val bundle = Bundle()
                                        bundle.putString(IntentConstant.TITLE, "Add Card")
                                        gotoClass(AddCardActivity::class.java, bundle)


                                    }
                                    .setNegativeText("LATER") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                            }


                        }
                        Status.LOADING -> {
                            mViewDataBinding.nameShimmer.visibility = View.VISIBLE
                            mViewDataBinding.userNameTV.visibility = View.GONE
                        }
                    }
                }
            }
        }*/

    private var shortCutListener: ListenerRegistration? = null


    private fun deleteDataFromFireStore(updateShortcut: ShortcutAddModel) {
        val db = Firebase.firestore
        val myIdentity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String

        val supports = db.collection("Shortcuts").whereEqualTo("identity", myIdentity)
            .whereEqualTo("id", updateShortcut.id)

        supports.get().addOnSuccessListener { result ->
            for (document in result) {
                val shortcuts = document.data as java.util.HashMap<*, *>
                val identity = shortcuts["identity"].toString()
                val myIdentity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
                if (identity == myIdentity) {


                    document.reference.delete().addOnSuccessListener {
                        hideDialog()
                        DebugMode.e(TAG, "Deleted successss ", TAG)
//                                getShortcuts()
                    }.addOnFailureListener { exception ->
                        DebugMode.e(TAG, "get failed with ", exception.message.toString())

                    }
                }
            }


        }.addOnFailureListener { exception ->
            DebugMode.e(TAG, "get failed with ", exception.message.toString())
        }


    }

    override fun onPause() {
        super.onPause()
//        currentDatabaseRef?.removeEventListener(currentBookingEventListener)
    }


    private var currentDatabaseRef: DatabaseReference? = null
    private fun realTimeDatabase() {
        val database = Firebase.database
        val identify = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
        currentDatabaseRef = database.getReference("notifications").child(identify).child("current")
        currentDatabaseRef?.addValueEventListener(currentBookingEventListener)


    }

    private val currentBookingEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (!snapshot.exists()) {
                return
            }
            try {
                DebugMode.e(TAG, "TRY VALUE IS ${snapshot.value}")
                gotoClass(CurrentRideActivity::class.java)


            } catch (e: Exception) {
                DebugMode.e(TAG, e.message.toString(), "Catch error firebase")
                DebugMode.e(TAG, "CATCH VALUE IS ${snapshot.value}")
            }


        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException())
        }

    }


}