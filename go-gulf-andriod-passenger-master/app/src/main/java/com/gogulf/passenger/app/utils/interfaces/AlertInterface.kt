package com.gogulf.passenger.app.utils.interfaces


import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.ui.shortcuts.ShortcutAddModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.PhoneAuthCredential
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.data.model.response.notification.NoticeModel


interface AdapterListener {
    fun onClicked(id: String)
    fun onDetail(bookingId: Int?, model: CurrentBookingResponseData)
}



interface AdapterClickListener {
    fun onDetail(bookingId: Int?, model: CurrentBookingResponseData)
}


interface HistoryAdapterListener {
    fun onClicked(movementId: String, bookingId: String)
//    fun onClicked(movementId: String, bookingId: String, model: Datum)
}



interface NoteInterface {
    fun onDone(note: String)
}

interface PinViewActionListener {
    fun onValid()
}

interface SimpleClick {
    fun onClicked()
}






interface OnDialogClicked {
    fun onClicked(id: Int)
}

interface ExtrasInterface {
    fun addData(id: String, value: String)

}

interface DatePickedListener {
    fun selectedDate(date: String)
}

interface ScheduledBookingListener {
    fun activeScheduled(isActive: Boolean)
    fun selectedDate(date: String)
}


interface TimePickedListener {
    fun selectedDate(timeFormatted: String, time24: String)
}


interface FinalMapLatLng {
    fun values(finalDestination: LatLng)
}

interface ShortcutClickListener {
    fun onClicked(title: String, id: Int)
}




interface FirebaseLoginListener {
    fun onError(error: String)
    fun onSuccess(success: PhoneAuthCredential)
    fun onCodeSent()
    fun onUserBlock(error: String)
    fun onTimeOut(error: String)
}


interface DateListener {
    fun value(date: String)
}


interface FAQClickListener {
    fun onClicked(name: String, id: String)
}


interface FirebaseOTPListener {
    fun onError(error: String)
    fun onSuccess(uid: String)
    fun onInValidOTPCode(error: String)
    fun onBlock(error: String)
}





interface VehicleSliderListener {
    fun count(count: Int)
}

interface CapacityValueListener {
    fun values(passenger: Int, luggage: Int)
}

interface CardUpdateListener {
    fun update(card: CardModels)
}

interface DashboardShortcutListener {
    fun editShortcut(shortcut: ShortcutAddModel)
    fun deleteShortcut(shortcut: ShortcutAddModel)
}

interface ShortcutMapListener {

    fun onClicked(shortcut: ShortcutAddModel)
}

interface ApiListener {
    fun onError(title: String?, message: String?)
    fun onLoading()
    fun onSuccess(data: JsonObject?)
}

interface AnyApiListener {
    fun onError(title: String?, message: String?)
    fun onLoading()
    fun onSuccess(data: Any?)
}
interface AnyApiListeners<T> {
    fun onError(title: String?, message: String?)
    fun onLoading()
    fun onSuccess(data: T?)
}
interface NotificationClickListener {
    fun onClicked(model: NoticeModel)
}