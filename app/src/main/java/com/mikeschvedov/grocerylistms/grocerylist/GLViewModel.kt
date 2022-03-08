package com.mikeschvedov.grocerylistms.grocerylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikeschvedov.grocerylistms.model.GroceryListItem
import java.util.*


class GLViewModel: ViewModel() {

    // Reference to Repository
    private val repository = GLRepository()

    private val _groceryListLiveData = MutableLiveData<List<GroceryListItem>>()
    val groceryListLiveData: LiveData<List<GroceryListItem>> = _groceryListLiveData


    fun fetchGroceryData(){
        // Passing the LiveData list as a parameter to get "filled" with response from database
        repository.fetchGroceryData(_groceryListLiveData)
    }

    fun updateIsItemBought(id: String, isBought: Boolean){
       repository.updateIsItemBought(id, isBought)
    }

    fun addNewItemToDatabase(newItem: GroceryListItem){
        repository.addNewItemToDatabase(newItem)
    }

    // Generate a unique ID
    fun generateNewID(): String {

        return UUID.randomUUID().toString()

    }
}