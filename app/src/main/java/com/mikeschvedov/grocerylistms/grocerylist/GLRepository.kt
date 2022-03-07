package com.mikeschvedov.grocerylistms.grocerylist

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mikeschvedov.grocerylistms.model.GroceryListItem

class GLRepository {

    private val database =
        FirebaseDatabase.getInstance("https://grocerylistms-default-rtdb.europe-west1.firebasedatabase.app")
    private val myRef = database.getReference("news_feed") //.orderByChild("name of element")



    fun fetchGroceryData(liveData: MutableLiveData<List<GroceryListItem>>) {

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groceryListItems: List<GroceryListItem> =
                    snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(GroceryListItem::class.java)!!
                            .copy(id = dataSnapshot.key!!) //setting the "id" to be equal to the "key" of the item (unique identifier)
                    }

                // posting the resulted "groceryListItems List" into the Mutable List of GroceryListItems
                liveData.postValue(groceryListItems)
            }

            override fun onCancelled(error: DatabaseError) {
                // Nothing to do
            }

        })


    }

    fun updateIsItemBought(id: String, isBought: Boolean){
        myRef.child(id).child("bought").setValue(isBought)
    }



}