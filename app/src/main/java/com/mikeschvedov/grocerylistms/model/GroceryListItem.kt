package com.mikeschvedov.grocerylistms.model

data class GroceryListItem (

    val id: String = "",
    val itemName: String = "",
    val itemAmount: String = "",
    val bought: Boolean = false,
    val timestamp: Long = 0

    //add:  date added
)


