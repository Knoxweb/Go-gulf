package com.gogulf.passenger.app.ui.shortcuts

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.utils.enums.DefaultInputType
import com.gogulf.passenger.app.utils.interfaces.ShortcutClickListener
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.objects.StringManager
import com.gogulf.passenger.app.BuildConfig
import com.gogulf.passenger.app.databinding.ActivityShortcutsScreenBinding
import kotlinx.coroutines.launch
import java.util.*

class ShortcutsActivity : BaseActivity<ActivityShortcutsScreenBinding>(), BaseNavigation {

    private lateinit var mViewDataBinding: ActivityShortcutsScreenBinding
    private lateinit var adapter: ShortcutAdapters
    private var shortcutList = ArrayList<ShortcutModel>()
    private lateinit var placesClient: PlacesClient
    private lateinit var geocoder: Geocoder
    private val TAG = "ShortcutActivity"
    private var addShortcuts: ShortcutAddModel? = null
    private var isPlaceSelected = false
    private var update = 0
    private var shortcutTitle = "Home"
    private var shortcutId = 0
    private lateinit var viewModel: AddShortcutsViewModel

    override fun getLayoutId(): Int = R.layout.activity_shortcuts_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityShortcutsScreenBinding

        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        mViewDataBinding.activity = this

        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            addShortcuts = bundle?.getSerializable(IntentConstant.SERIAL) as ShortcutAddModel
            update = bundle.getInt(IntentConstant.ID)!!
        } catch (e: Exception) {
            addShortcuts = null
            update = 0
        }

        viewModel = ViewModelProvider(this, AddShortCutsViewModelFactory(update))[AddShortcutsViewModel::class.java]

        adapter = ShortcutAdapters(this, shortcutList, object : ShortcutClickListener {
            override fun onClicked(title: String, id: Int) {
                mViewDataBinding.addAddressTitle.text = title
                viewModel.name.set(title.lowercase())
                shortcutTitle = title
                shortcutId = id
            }

        })
        mViewDataBinding.address = "Address"
        mViewDataBinding.addAddressTitle.setHintVisible(View.VISIBLE)
        mViewDataBinding.addAddressTitle.setFocus(false)

        mViewDataBinding.addAddressTitle.text = "Home"
        viewModel.name.set("home")

        initPlaces()


        mViewDataBinding.addAddressTitle.setMaxLength(10)
        mViewDataBinding.addAddressTitle.setInputType(DefaultInputType.CapSentence)


//        mViewDataBinding.toolbar.onClicked { onBackPressed() }
        mViewDataBinding.shortcutRecyclerView.layoutManager = GridLayoutManager(this, 5)
        mViewDataBinding.shortcutRecyclerView.adapter = adapter

        addList()
        if (update!=0 && addShortcuts != null) {
            isPlaceSelected = true
            startName = addShortcuts?.address!!
            shortcutId = update
            shortcutTitle = addShortcuts?.name!!
            startGeoLocation = LatLng(addShortcuts?.lat!!, addShortcuts?.lng!!)
            mViewDataBinding.address = addShortcuts?.address
            mViewDataBinding.addAddressTitle.text = addShortcuts?.name
            adapter.highLightSelected(addShortcuts?.name!!)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
        viewModel.customLoader = CustomLoader(this)


        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.hide()

                }

                if (it.error != null) {
                    errorAlertDialog(it.error.title, it.error.message)
                    viewModel.resetError()
                }

                if (it.isSuccess && it.successMessage != null) {
                    CustomAlertDialog(this@ShortcutsActivity).setTitle(it.successMessage?.title)
                        .setMessage(it.successMessage?.message).setPositiveText("OK") { dialog, _ ->
                            finish()
                        }.setCancellable(false).show()

                    viewModel.resetSuccessMessage()


                }
            }
        }


    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@ShortcutsActivity, cls, true)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this@ShortcutsActivity, cls, bundle, true)
    }


    private fun initPlaces() {
        Places.initialize(baseContext, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(this)
        geocoder = Geocoder(this, Locale.ENGLISH)

        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        mViewDataBinding.addAddress.cardContainer.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setLocationBias(
                    RectangularBounds.newInstance(
                        LatLng(latitude - 0.1, longitude - 0.1),
                        LatLng(latitude + 0.1, longitude + 0.1)
                    )
                ).build(baseContext)
            startDestination.launch(
                intent
            )
        }
    }


    private var startName = ""
    private var startDestination =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(result.data!!)
                Log.e("ShortcutActivity", "Place: $place " + place.name + ", " + place.id)
                startGeoLocation = place.latLng

                isPlaceSelected = true
                mViewDataBinding.addAddress.hintTextView.text = place.name
                var addresses =
                    geocoder.getFromLocation(place.latLng!!.latitude, place.latLng!!.longitude, 1)!!
                for (adds in addresses) {
                    DebugMode.e(TAG, "address $adds")
//                    var placeName = "${place.name} ${adds.getAddressLine(0)}"
                    var placeName =
                        StringManager.concatSameString(place.name, adds.getAddressLine(0))
                    DebugMode.e(TAG, "PLACE NAME $placeName", "sadjhagjasdgajhdgahjashd")
                    mViewDataBinding.addAddress.hintTextView.text = placeName
                    startName = placeName
                    viewModel.address.set(placeName)
                    viewModel.lat.set(place.latLng!!.latitude.toString())
                    viewModel.lng.set(place.latLng!!.longitude.toString())



                }


            } else if (result.resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(result.data!!)
                Log.e("ShortcutActivity", "start-> " + status.statusMessage)
            }
            return@registerForActivityResult


        }


    private fun addList() {
        shortcutList.clear()
        addData("Home", R.drawable.ic_s_home)
        addData("Office", R.drawable.ic_s_address)
        addData("Airport", R.drawable.ic_s_flight)
        addData("Bank", R.drawable.ic_s_bank)
        addData("School", R.drawable.ic_s_school)
        addData("Restaurant", R.drawable.ic_s_restaurant)
        addData("Hospital", R.drawable.ic_s_hospital)
        addData("Doctor", R.drawable.ic_s_doctor)
        addData("Store", R.drawable.ic_s_store)
        addData("College", R.drawable.ic_s_college)
        addData("Cinema", R.drawable.ic_s_cinema)
        addData("Meeting", R.drawable.ic_s_meeting)
        addData("Theatre", R.drawable.ic_s_theatre)
        addData("Wedding", R.drawable.ic_s_wedding)
        addData("Fitness", R.drawable.ic_s_fitness)
        addData("Gas St", R.drawable.ic_s_gas_st)
        addData("Charge St", R.drawable.ic_s_charge)
        addData("Mosque", R.drawable.ic_s_mosque)
        addData("Family", R.drawable.ic_s_family)
        addData("Pool", R.drawable.ic_s_pool)
        addData("Others", R.drawable.ic_s_default)
        adapter.notifyDataSetChanged()
    }

    private fun addData(title: String, icon: Int) {
        shortcutList.add(ShortcutModel(title, icon))
    }

    private fun addShortcutFirebase() {

        addShortcutToFirestore(getModel())

    }

    private fun getModel(): ShortcutAddModel {
        return ShortcutAddModel(
            startName,
            shortcutTitle,
            Integer.parseInt(shortcutId.toString()),
            preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String,
            startGeoLocation?.latitude ?: 0.00,
            startGeoLocation?.longitude ?: 0.00,
            shortcutTitle
        )

    }

    private fun updateFirestoreData() {

        updateShortcutToFirestore(getModel())

    }

    override fun onValidated() {
        if (startName.isNullOrEmpty()) {
            errorAlertDialog("Empty", "Please choose place to continue")

        } else {

//            showDialog()
//            viewModel.addAddress()
//            checkIfAlreadyExists(getModel())

            val existingItem = viewModel.mShortcutList.find { it.name == viewModel.name.get() }

            if (existingItem != null) {
                viewModel.editAddress(existingItem.id)
            } else {
                viewModel.addAddress()
            }

        }
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }


    private fun addShortcutToFirestore(shortcutAddModel: ShortcutAddModel) {
        val db = Firebase.firestore
        val supports = db.collection("Shortcuts").document()
        supports.set(shortcutAddModel).addOnSuccessListener {
            hideDialog()
            onBackPress()

        }.addOnFailureListener { exception ->
            DebugMode.e(TAG, "get failed with ", exception.message.toString())
        }


    }

    private fun updateShortcutToFirestore(updateShortcut: ShortcutAddModel) {
        val db = Firebase.firestore
        val myIdentity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String

        val supports = db.collection("Shortcuts").whereEqualTo("identity", myIdentity)
            .whereEqualTo("id", update)

        supports.get().addOnSuccessListener { result ->
            for (document in result) {
                val shortcuts = document.data as HashMap<*, *>
                val identity = shortcuts["identity"].toString()
                val myIdentity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
                if (identity == myIdentity) {


                    document.reference.update(
                        mapOf(
                            "address" to updateShortcut.address,
                            "icon" to updateShortcut.icon,
                            "id" to updateShortcut.id,
                            "identity" to updateShortcut.identity,
                            "lat" to updateShortcut.lat,
                            "lng" to updateShortcut.lng,
                            "title" to updateShortcut.title,
                        )
                    ).addOnSuccessListener {
                        hideDialog()
                        DebugMode.e(TAG, "$updateShortcut", "aba update garne")
                        onBackPress()
                    }.addOnFailureListener { exception ->
                        DebugMode.e(TAG, "get failed with ", exception.message.toString())

                    }
                }
            }


        }.addOnFailureListener { exception ->
            DebugMode.e(TAG, "get failed with ", exception.message.toString())
        }


    }



}