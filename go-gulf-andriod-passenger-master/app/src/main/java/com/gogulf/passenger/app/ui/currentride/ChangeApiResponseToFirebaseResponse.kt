//package com.slyyk.passenger.app.ui.currentride
//
//import com.google.gson.annotations.SerializedName
//import com.slyyk.passenger.app.data.model.base.BaseData
//import com.slyyk.passenger.app.data.model.response.bookings.PolylinePoints
//import com.slyyk.passenger.app.data.model.response.currentride.BookingApi
//import com.slyyk.passenger.app.data.model.response.currentride.CardApi
//import com.slyyk.passenger.app.data.model.response.currentride.CurrentRideApiModel
//import com.slyyk.passenger.app.data.model.response.currentride.CurrentRideBaseModels
//import com.slyyk.passenger.app.data.model.response.currentride.DriverApi
//import com.slyyk.passenger.app.data.model.response.currentride.LocationApi
//
//object ChangeApiResponseToFirebaseResponse {
//
//    fun getCurrentRideBaseModels(req: CurrentRideBaseModels) {
//        val data = CurrentRideApiModel(
//            driverJobAcceptedLocation = LocationApi(lat=req.data.driver_job_accepted_location.lat,lng=req.data.driver_job_accepted_location.lng),
//            driverCurrentLocation = LocationApi(req.data.driver_current_location.lat,req.data.driver_current_location.lng),
//            dropoffLocation = LocationApi(req.data.pickup_location.lat,req.data.pickup_location.lng),
//            pickupLocation = LocationApi(),
//            driver = DriverApi(),
//            booking = BookingApi(),
//            card = CardApi(),
//            duration = req.data?.duration,
//            polylinePoints = PolylinePoints(),
//            distance = req.data?.distance
//        )
//
//
//        val res = BaseData<CurrentRideApiModel>(
//            title = req.title ?: "",
//            message = req.message ?: "",
//            mode = req.mode ?: "",
//            data = data
//        )
//    }
//}