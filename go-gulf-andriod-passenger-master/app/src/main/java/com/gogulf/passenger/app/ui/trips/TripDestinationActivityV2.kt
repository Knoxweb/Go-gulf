package com.gogulf.passenger.app.ui.trips

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.gogulf.passenger.app.data.model.CalculateFareRequestBody
import com.gogulf.passenger.app.data.model.LocationModel
import com.gogulf.passenger.app.ui.locationselector.LocationSelectorActivity
import com.gogulf.passenger.app.ui.locationselector.LocationSelectorUIState
import com.gogulf.passenger.app.ui.plantrip.PlanTripActivity
import com.gogulf.passenger.app.utils.LocationUtils
import com.gogulf.passenger.app.utils.LocationUtils.getFullAddress
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityTripDestinationV2Binding

class TripDestinationActivityV2 : AppCompatActivity() {

    private lateinit var binding: ActivityTripDestinationV2Binding
    private lateinit var viewModel: TripDestinationV2ViewModel
    private val PICKUP_LOCATION_SELECTOR_REQUEST_CODE = 0
    private val DROP_OFF_LOCATION_SELECTOR_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemBarUtil.enableEdgeToEdge(this)
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityTripDestinationV2Binding>(
            this, R.layout.activity_trip_destination_v2
        ).also {
            binding = it
            it.lifecycleOwner = this
            binding.activity = this
            viewModel = ViewModelProvider(this)[TripDestinationV2ViewModel::class.java]
            binding.viewModel = viewModel
        }



        binding.btnDropOffLocation.background =
            AppCompatResources.getDrawable(this, R.drawable.background_active_bottom)



        binding.btnPickUpLocation.setOnClickListener {
            viewModel.activeLocation = 0

            it.background = AppCompatResources.getDrawable(this, R.drawable.background_active_top)
            binding.btnDropOffLocation.background =
                AppCompatResources.getDrawable(this, R.drawable.background_bottom_in_list)


            val intent = Intent(this, LocationSelectorActivity::class.java)
            startActivityForResult(intent, PICKUP_LOCATION_SELECTOR_REQUEST_CODE)
        }

        binding.btnDropOffLocation.setOnClickListener {
            viewModel.activeLocation = 1

            it.background =
                AppCompatResources.getDrawable(this, R.drawable.background_active_bottom)
            binding.btnPickUpLocation.background =
                AppCompatResources.getDrawable(this, R.drawable.background_top_in_list)

            val intent = Intent(this, LocationSelectorActivity::class.java)
            startActivityForResult(intent, DROP_OFF_LOCATION_SELECTOR_REQUEST_CODE)
        }

        binding.shortcutRecyclerView.layoutManager = GridLayoutManager(this, 5)
        binding.shortcutRecyclerView.adapter = viewModel.shortcutAdapter.value

        LocationUtils.myCurrentLocation.value?.let {
            viewModel.updateSelectedPickUpLocation(
                LocationModel(
                    getFullAddress(it.latitude, it.longitude), it.latitude, it.longitude
                )
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICKUP_LOCATION_SELECTOR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                val returnString: LocationSelectorUIState =
                    data?.getSerializableExtra("data") as LocationSelectorUIState

                viewModel.updateSelectedPickUpLocation(
                    LocationModel(
                        returnString.selectedAddress,
                        returnString.selectedLat,
                        returnString.selectedLog
                    )
                )

            }
        }

        if (requestCode == DROP_OFF_LOCATION_SELECTOR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                val returnString: LocationSelectorUIState =
                    data?.getSerializableExtra("data") as LocationSelectorUIState
                viewModel.updateSelectedDropOffLocation(
                    LocationModel(
                        returnString.selectedAddress,
                        returnString.selectedLat,
                        returnString.selectedLog
                    )
                )
            }
        }

    }

    private fun isValidInput(): Boolean {
        var returnBoolean = true
        if (viewModel.uiState.value.selectedPickupLocation == null && viewModel.uiState.value.selectedDropOffLocation == null) {
            errorAlertDialog("Missing", "Please select a pickup and drop off location")
            returnBoolean = false
        } else if (viewModel.uiState.value.selectedPickupLocation == null) {
            errorAlertDialog("Missing", "Please select a pickup location")
            returnBoolean = false
        } else if (viewModel.uiState.value.selectedDropOffLocation == null) {
            errorAlertDialog("Missing", "Please select a drop off location")
            returnBoolean = false
        }
        return returnBoolean
    }

    fun errorAlertDialog(title: String?, message: String?) {
        CustomAlertDialog(this).setTitle(title).setMessage(message)
            .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }.setCancellable(false).show()
    }

    @SuppressLint("DefaultLocale")
    fun onDoneClicked() {
        if (isValidInput()) {
            val calculateFareRequestBody = CalculateFareRequestBody()
            calculateFareRequestBody.pickup_address =
                viewModel.uiState.value.selectedPickupLocation?.address ?: ""
            calculateFareRequestBody.pickup_address_lat =
                viewModel.uiState.value.selectedPickupLocation?.latitude?.toString()
            calculateFareRequestBody.pickup_address_lng =
                viewModel.uiState.value.selectedPickupLocation?.longitude?.toString()
            calculateFareRequestBody.drop_address =
                viewModel.uiState.value.selectedDropOffLocation?.address ?: ""
            calculateFareRequestBody.drop_address_lat =
                viewModel.uiState.value.selectedDropOffLocation?.latitude?.toString()
            calculateFareRequestBody.drop_address_lng =
                viewModel.uiState.value.selectedDropOffLocation?.longitude?.toString()

            val bundle = Bundle()
            bundle.putSerializable(IntentConstant.SERIAL, calculateFareRequestBody)
            val intent = Intent(this, PlanTripActivity::class.java)
            intent.putExtra(IntentConstant.BUNDLE, bundle)
            startActivity(intent)
        }
    }

}