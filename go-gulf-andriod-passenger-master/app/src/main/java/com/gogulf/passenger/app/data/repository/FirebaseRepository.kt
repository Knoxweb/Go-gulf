package com.gogulf.passenger.app.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.utils.PrefEntity
import com.gogulf.passenger.app.utils.Preferences

class FirebaseRepository {
    private val TAG = "FirebaseRepository"
    private val firestoreDB = Firebase.firestore

    val database = Firebase.database

    fun getTermsConditions(): CollectionReference = firestoreDB.collection("contents")

    fun getPolicies(): CollectionReference = firestoreDB.collection("policy_contents")

    fun getShortcuts(): CollectionReference = firestoreDB.collection("Shortcuts")

    fun nearbyDrivers(): CollectionReference = firestoreDB.collection("nearby_drivers")

    fun trackDrivers(): DatabaseReference = database.getReference("nearbyDrivers")

    fun trackCurrentRide(): DatabaseReference = database.getReference("currentRide")

    fun getBaseDoc(): DocumentReference =
        firestoreDB.collection("passengers").document(getFirebaseReference())


    private fun getFirebaseReference(): String {
        val fromPref = Preferences.getPreference(App.baseApplication, PrefEntity.FIREBASE_REFERENCE)
        return fromPref
    }

    fun getApiCollection(): CollectionReference =
        firestoreDB.collection("api")
    fun getNearByCollection(): CollectionReference =
        firestoreDB.collection("nearby_drivers")

}