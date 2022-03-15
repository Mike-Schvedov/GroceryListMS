package com.mikeschvedov.grocerylistms.grocerylist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.mikeschvedov.grocerylistms.model.GroceryListItem
import com.mikeschvedov.grocerylistms.notifications.PushNotification
import com.mikeschvedov.grocerylistms.notifications.RetrofitInstance
import com.mikeschvedov.grocerylistms.utils.Constants.Companion.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GLRepository {

    private val database = FirebaseDatabase.getInstance(DATABASE_NAME)                               // Creating an Instance of our database
    private val myRef = database.reference                                                           // Getting the reference of the root


    var clicked: Boolean = false                                                                     // Checking if actually clicked button so function will not run by itself


    fun fetchGroceryData(liveData: MutableLiveData<List<GroceryListItem>>) {

        myRef
            .orderByChild("itemName")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val groceryListItems: List<GroceryListItem> =
                        snapshot.children.map { dataSnapshot ->
                            dataSnapshot.getValue(GroceryListItem::class.java)!!
                                .copy(id = dataSnapshot.key!!)                                       //setting the "id" to be equal to the "key" of the item (unique identifier)
                        }

                                                                                                     // posting the resulted "groceryListItems List" into the Mutable List of GroceryListItems
                    liveData.postValue(groceryListItems)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Nothing to do
                }

            })

    }


    fun updateIsItemBought(id: String, isBought: Boolean) {
        myRef.child(id).child("bought").setValue(isBought)
    }

    fun addNewItemToDatabase(newItem: GroceryListItem) {
        myRef.child(newItem.id).setValue(newItem)
    }


    fun deleteEntireList() {
       myRef.removeValue()
    }

    fun deleteOnlySelected() {

        clicked = true

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && clicked) {
                    dataSnapshot.children.forEach {

                        val itemsKey = it.key                                                        // For each child of our reference(root database) we get the item's (aka "it") key.
                        val itemsBought: Boolean = it.child("bought").value as Boolean          // We get the value of "bought", a child of the item ("it").


                        if (itemsBought) {                                                           // If the current "bought" value is true (checked) then remove the item (according to the key - "itemsKey")
                            if (itemsKey != null) {
                                myRef.child(itemsKey).removeValue()
                            }
                        }
                    }

                    clicked = false                                                                  // Setting back to false, because we finished iterating
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    /*                                          SEND NOTIFICATION                                              */

    fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("sendNotification", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("sendNotification", response.errorBody().toString())
                }

            } catch (e: Exception) {
                Log.e("sendNotification", e.toString())
            }
        }

    fun subscribeDeviceToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(NOTIFICATION_TOPIC)
    }
}