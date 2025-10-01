package com.gogulf.passenger.app.ui.trips

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gogulf.passenger.app.data.model.request.CreateQuote
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.getaride.ShortcutAdapter
import com.gogulf.passenger.app.ui.plantrip.PlanTripActivity
import com.gogulf.passenger.app.ui.shortcuts.ShortcutAddModel
import com.gogulf.passenger.app.ui.shortcuts.ShortcutsActivity
import com.gogulf.passenger.app.utils.interfaces.ShortcutMapListener
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.utils.objects.StringManager
import com.gogulf.passenger.app.BuildConfig
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityTripDestinationScreenBinding
import java.util.*

class TripDestinationActivity : BaseActivity<ActivityTripDestinationScreenBinding>(),
    BaseNavigation {

    private lateinit var mViewDataBinding: ActivityTripDestinationScreenBinding

    private val TAG = "TripDestinationActivity"
    private lateinit var placesClient: PlacesClient
    private lateinit var geocoder: Geocoder
    private lateinit var request: FetchPlaceRequest
    private var isStartDestinationSelected = false
    private var isEndDestinationSelected = false

    private var shortcutAddModel: ShortcutAddModel? = null
    private var startName = ""
    private var endName = ""


    private var mShortcutList = ArrayList<ShortcutAddModel>()
    private lateinit var adapter: ShortcutAdapter

    override fun getLayoutId(): Int = R.layout.activity_trip_destination_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityTripDestinationScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        initPlaces()

        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            shortcutAddModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getSerializable(IntentConstant.SERIAL, ShortcutAddModel::class.java)
            } else {
                bundle?.getSerializable(IntentConstant.SERIAL) as ShortcutAddModel
            }
        } catch (e: Exception) {
            DebugMode.e(TAG, e.message.toString(), TAG)
        }

        adapter = ShortcutAdapter(context = this@TripDestinationActivity,
            mList = mShortcutList,
            showAll = true,
            shortcutMapListener = object : ShortcutMapListener {
                override fun onClicked(shortcut: ShortcutAddModel) {
                    if (shortcut.title=="Add"){
//                        val bundle = Bundle()
//                        if (endName.isNotEmpty()) {
//                            bundle.putString(IntentConstant.NAME, endName)
//                            bundle.putDouble(IntentConstant.LNG, destinationGeoLocation?.longitude!!)
//                            bundle.putDouble(IntentConstant.LAT, destinationGeoLocation?.latitude!!)
//                            bundle.putBoolean(IntentConstant.IS_PLACE_SELECTED, true)
//                        }
                        gotoClass(ShortcutsActivity::class.java)
                    }else{
                        shortcutAddModel = shortcut
                        getDestination()

                    }
                }
            })


        mViewDataBinding.addShortcut.root.visibility=View.GONE
        mViewDataBinding.addShortcut.shortcutTitle.text = "Add"
        mViewDataBinding.addShortcut.shortcutImage.setImageDrawable(
            ContextCompat.getDrawable(
                this@TripDestinationActivity,
                R.drawable.ic_menu_add
            )
        )

        mViewDataBinding.addShortcut.layoutContainer.setOnClickListener {
            gotoClass(ShortcutsActivity::class.java)
        }


        mViewDataBinding.shortcutRecyclerView.hasFixedSize()
        mViewDataBinding.shortcutRecyclerView.layoutManager =GridLayoutManager(this, 5)
        mViewDataBinding.shortcutRecyclerView.adapter = adapter
        if (shortcutAddModel != null) {
            getDestination()

        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        getShortcuts()
    }

    override fun onStop() {
        super.onStop()
    }


    private fun initPlaces() {
        Places.initialize(baseContext, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(this)
        geocoder = Geocoder(this, Locale.ENGLISH)

        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)


        val latitude = (preferenceHelper.getValue(PrefConstant.LATITUDE, "0") as String).toDouble()
        val longitude =
            (preferenceHelper.getValue(PrefConstant.LONGITUDE, "0") as String).toDouble()


        var addresses =
            geocoder.getFromLocation(latitude, longitude, 1)
        for (adds in addresses!!) {
            startGeoLocation = LatLng(latitude, longitude)
            isStartDestinationSelected = true
            DebugMode.e(TAG, "address $adds")
            var placeName = adds.getAddressLine(0)
            mViewDataBinding.searchContainer.startField.text = placeName
            startName = placeName
        }

        mViewDataBinding.searchContainer.startField.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)

                .setLocationBias(
                    RectangularBounds.newInstance(
                        LatLng(latitude - 0.1, longitude - 0.1),
                        LatLng(latitude + 0.1, longitude + 0.1)
                    )
                )
                .build(baseContext)
//            intent.putExtra("requestCode", START_AUTOCOMPLETE_REQUEST_CODE)
            startDestination.launch(
                intent
            )
        }


        mViewDataBinding.searchContainer.destinationField.setOnClickListener {
            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .setLocationBias(
                        RectangularBounds.newInstance(
                            LatLng(latitude - 0.1, longitude - 0.1),
                            LatLng(latitude + 0.1, longitude + 0.1)
                        )
                    )
                    .build(baseContext)
//            intent.putExtra("requestCode", DESTINATION_AUTOCOMPLETE_REQUEST_CODE)
            toDestination.launch(
                intent
            )
        }
    }

    private fun isValidated(): Boolean =
        if (!isStartDestinationSelected) {
            errorAlertDialog("Missing", "Start destination is required")
            false
        } else if (!isEndDestinationSelected) {
            errorAlertDialog("Missing", "End destination is required")
            false
        } else if (startName == endName) {
            errorAlertDialog("Same", "Start and end destination is same")
            false
        } else true


    private fun checkValidations() {
        if (isValidated()) {
            onSubmit()
        }
    }

    override fun onValidated() {
        checkValidations()
//        onSubmit()
    }

    override fun onBackPress() {
        onBackPressed()
    }

    private fun gotoPlanTrip() {
        val createQuote = CreateQuote()
        createQuote.fromLocation = startName
        createQuote.fromLat = String.format("%.6f", startGeoLocation?.latitude)
        createQuote.fromLng = String.format("%.6f", startGeoLocation?.longitude)

        createQuote.toLocation =
            endName
        createQuote.toLat = String.format("%.6f", destinationGeoLocation?.latitude)
        createQuote.toLng = String.format(
            "%.6f", destinationGeoLocation?.longitude
        )

        DebugMode.e(TAG, createQuote.toString())
        val bundle = Bundle()
        bundle.putSerializable(IntentConstant.SERIAL, createQuote)
        gotoClass(PlanTripActivity::class.java, bundle)
    }

    private fun getDestination() {
        isEndDestinationSelected = true
        endName = shortcutAddModel?.address!!
        mViewDataBinding.searchContainer.destinationField.text = endName
        destinationGeoLocation = LatLng(shortcutAddModel?.lat!!, shortcutAddModel?.lng!!)

    }

    override fun onSubmit() {
        gotoPlanTrip()
//        gotoClass(PlanTripActivity::class.java)
    }


    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@TripDestinationActivity, cls, false)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this@TripDestinationActivity, cls, bundle, false)
    }


    private var startDestination =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val intent = result.data!!

//            if (data != null) {
            if (result.resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(result.data!!)
                Log.e(TAG, "Place: $place " + place.name + ", " + place.id)
                startGeoLocation = place.latLng
                mViewDataBinding.searchContainer.startField.text = place.name
                startName = place.name!!
                isStartDestinationSelected = true
                var addresses =
                    geocoder.getFromLocation(place.latLng!!.latitude, place.latLng!!.longitude, 1)!!
                for (adds in addresses) {
                    DebugMode.e(TAG, "address $adds")
//                    var placeName = "${place.name}, ${adds.getAddressLine(0)}"
                    var placeName =
                        StringManager.concatSameString(place.name, adds.getAddressLine(0))
                    DebugMode.e(TAG, "PLACE NAME $placeName", "sadjhagjasdgajhdgahjashd")
                    mViewDataBinding.searchContainer.startField.text = placeName

                    startName = placeName
                }


            } else if (result.resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(result.data!!)
                Log.e(TAG, "start-> " + status.statusMessage)
            }
            return@registerForActivityResult
//            }


        }


    private var toDestination =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->


//            if (data != null) {
            if (result.resultCode == RESULT_OK) {
                val place =
                    Autocomplete.getPlaceFromIntent(result.data!!)
                Log.e(
                    TAG,
                    "Place: $place" + place.name + ", " + place.id
                )
                destinationGeoLocation = place.latLng
                mViewDataBinding.searchContainer.destinationField.text =
                    place.name
                endName = place.name!!
                isEndDestinationSelected = true
                var addresses =
                    geocoder.getFromLocation(place.latLng!!.latitude, place.latLng!!.longitude, 1)
                for (adds in addresses!!) {
                    DebugMode.e(TAG, "address $adds")
//                    var placeName = "${place.name} ${adds.getAddressLine(0)}"
                    var placeName =
                        StringManager.concatSameString(place.name, adds.getAddressLine(0))
                    DebugMode.e(TAG, "PLACE NAME $placeName", "sadjhagjasdgajhdgahjashd")
                    mViewDataBinding.searchContainer.destinationField.text =
                        placeName
                    endName = placeName
                }

            } else if (result.resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status =
                    Autocomplete.getStatusFromIntent(result.data!!)
                Log.e(
                    TAG,
                    "destination-> " + status.statusMessage
                )
            }
            return@registerForActivityResult
//            }

        }

    private var shortcutListener: ListenerRegistration? = null

    @SuppressLint("NotifyDataSetChanged")
    private fun getShortcuts() {
        val db = Firebase.firestore
        val shortcutList = db.collection("Shortcuts")
        /* shortcutList.get()
             .addOnSuccessListener { result ->
                 mShortcutList.clear()
                 for (document in result) {
                     val shortcuts = document.data as HashMap<*, *>

                     val identity = shortcuts["identity"].toString()
                     val myIdentity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
                     if (identity == myIdentity) {

                         DebugMode.e(TAG, "Retreive successss $shortcuts ", TAG)
                         val shortcutAddModel = ShortcutAddModel(
                             shortcuts["address"].toString(),
                             shortcuts["icon"].toString(),
                             shortcuts["id"].toString(),
                             shortcuts["identity"].toString(),
                             shortcuts["lat"].toString().toDouble(),
                             shortcuts["lng"].toString().toDouble(),
                             shortcuts["title"].toString(),
                         )
                         mShortcutList.add(shortcutAddModel)
                     }
                 }
                 adapter.notifyDataSetChanged()

             }
             .addOnFailureListener { exception ->
                 DebugMode.e(TAG, "get failed with ", exception.message.toString())
             }*/

        shortcutListener = shortcutList.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                mShortcutList.clear()
                for (document in snapshot) {
                    val shortcuts = document.data as HashMap<*, *>
                    val identity = shortcuts["identity"].toString()
                    val myIdentity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
                    if (identity == myIdentity) {
                        val shortcutAddModel = ShortcutAddModel(
                            shortcuts["address"].toString(),
                            shortcuts["icon"].toString(),
                            Integer.parseInt(shortcuts["id"].toString()),
                            shortcuts["identity"].toString(),
                            shortcuts["lat"].toString().toDouble(),
                            shortcuts["lng"].toString().toDouble(),
                            shortcuts["title"].toString(),
                        )
                        mShortcutList.add(shortcutAddModel)
                    }

                }
                val shortcutAdd= ShortcutAddModel(
                    "",
                    "add",
                    0,
                    "",
                    0.0,
                    0.0,
                    "Add",
                )
                mShortcutList.add(shortcutAdd)
                adapter.notifyDataSetChanged()
            } else {
                Log.d(TAG, "Current data: null")
            }
        }


    }
}