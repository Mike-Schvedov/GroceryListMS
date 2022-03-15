package com.mikeschvedov.grocerylistms.grocerylist

import android.content.Context
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.mikeschvedov.grocerylistms.databinding.ActivityMainBinding
import com.mikeschvedov.grocerylistms.model.GroceryListItem
import com.mikeschvedov.grocerylistms.notifications.FirebaseNotificationService
import com.mikeschvedov.grocerylistms.notifications.NotificationData
import com.mikeschvedov.grocerylistms.notifications.PushNotification
import java.util.*


class GLViewModel: ViewModel() {

    // Reference to Repository
    private val repository = GLRepository()

    private val _groceryListLiveData = MutableLiveData<List<GroceryListItem>>()
    val groceryListLiveData: LiveData<List<GroceryListItem>> = _groceryListLiveData

    /*                            CONNECTION TO THE REPOSITORY                                      */

    fun fetchGroceryData(){
        // Passing the LiveData list as a parameter to get
        // "filled" with response from database
        repository.fetchGroceryData(_groceryListLiveData)
    }

    fun updateIsItemBought(id: String, isBought: Boolean){
       repository.updateIsItemBought(id, isBought)
    }

    fun addNewItemToDatabase(newItem: GroceryListItem){
        repository.addNewItemToDatabase(newItem)
    }

    fun deleteOnlySelected(){
        repository.deleteOnlySelected()
    }

    fun deleteEntireList(){
        repository.deleteEntireList()
    }

    /*                              FUNCTIONS USED IN VIEW                                        */

    fun floatingCancelButtonNPressed(binding: ActivityMainBinding) {
        binding.addnewitemLayout.isVisible = false
        binding.floatingButtonAddBTN.isVisible = true
        binding.floatingButtonCancelBTN.isVisible = false
        // Remove existing text from inputfields
        binding.itemNameInputfield.text?.clear()
        binding.itemAmountInputfield.text?.clear()
    }

    fun floatingAddButtonPressed(binding: ActivityMainBinding) {
        binding.addnewitemLayout.isVisible = true
        binding.floatingButtonAddBTN.isVisible = false
        binding.floatingButtonCancelBTN.isVisible = true
    }


    fun clickedDeleteEntireListBTN(binding: ActivityMainBinding) {
        TODO("Not yet implemented")
    }


    // --------------------------------- DELETION ALERTS LAYOUTS ------------------------------- //

    fun openingAlertAllLayout(binding: ActivityMainBinding) {

        // show transparent background //
        binding.includeToolbar.alertAllLayoutDarkBackground.isVisible = true
        // show alert layout //
        binding.includeToolbar.alertAllLayout.isVisible = true
        // hide toolbar menu //
        binding.includeToolbar.toolbarmenuLayout.isVisible = false
        //hide floating button
        binding.floatingButtonAddBTN.isVisible = false
        // disable toolbar button
        binding.includeToolbar.toolbarmenuButton.isClickable = false

    }

    fun closingAlertAllLayout(binding: ActivityMainBinding) {

        // hide transparent Background
        binding.includeToolbar.alertAllLayoutDarkBackground.isVisible = false
        // hide alert layout
        binding.includeToolbar.alertAllLayout.isVisible = false
        // enable toolbar menu button again
        binding.includeToolbar.toolbarmenuButton.isClickable = true
        //show floating button
        binding.floatingButtonAddBTN.isVisible = true

    }

    fun openingAlertSelectedLayout(binding: ActivityMainBinding) {
        // show transparent background //
        binding.includeToolbar.alertSelectedLayoutDarkBackground.isVisible = true
        // show alert layout //
        binding.includeToolbar.alertSelectedLayout.isVisible = true
        // hide toolbar menu //
        binding.includeToolbar.toolbarmenuLayout.isVisible = false
        //hide floating button
        binding.floatingButtonAddBTN.isVisible = false
        // disable toolbar button
        binding.includeToolbar.toolbarmenuButton.isClickable = false
    }

    fun closingAlertSelectedLayout(binding: ActivityMainBinding) {
        // hide transparent Background
        binding.includeToolbar.alertSelectedLayoutDarkBackground.isVisible = false
        // hide alert layout
        binding.includeToolbar.alertSelectedLayout.isVisible = false
        // enable toolbar menu button again
        binding.includeToolbar.toolbarmenuButton.isClickable = true
        //show floating button
        binding.floatingButtonAddBTN.isVisible = true
    }



    // --------------------------------- ADD NEW ITEM LAYOUT ------------------------------------ //

    fun clickingAddNewItem(binding: ActivityMainBinding, context: Context) {
        val itemName = binding.itemNameInputfield.text.toString()
        val itemAmount = binding.itemAmountInputfield.text.toString()

        println("INFO FROM INPUT FIELDS: ITEM NAME: $itemName  ITEM AMOUNT: $itemAmount")

        if (itemName.isBlank()) {
            // If the user did not enter something into the item name input field (amount field is not mandatory)
            Toast.makeText(context, "חסר מידע!", Toast.LENGTH_SHORT).show()
        } else {

            // Generate unique id
            val newID: String = generateNewID()

            // Get current timestamp
            val currentTime: Date = Date()
            val currentTimeStamp = currentTime.time

            val newItem: GroceryListItem =
                GroceryListItem(newID, itemName, itemAmount, false, currentTimeStamp)
            addNewItemToDatabase(newItem)
            floatingCancelButtonNPressed(binding)

            sendNotificationOnItemAddition()
        }
    }



    /*                                          MISC                                              */

    fun generateNewID(): String {
        return UUID.randomUUID().toString()
    }

    fun sendNotificationOnItemAddition() {

        //EXAMPLE
        val itemTitle: String = "רשימת הקניות שלי"
        if(itemTitle.isNotEmpty()){
            PushNotification(
                NotificationData(itemTitle, "פריט חדש נוסף לרשימה."),
                NOTIFICATION_TOPIC
            ).also {
                repository.sendNotification(it)
            }
        }



    }

    fun subscribeDeviceToTopic() {
        repository.subscribeDeviceToTopic()
    }




}