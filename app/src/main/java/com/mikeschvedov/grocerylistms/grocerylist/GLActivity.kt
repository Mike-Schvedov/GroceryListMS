package com.mikeschvedov.grocerylistms.grocerylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mikeschvedov.grocerylistms.R
import com.mikeschvedov.grocerylistms.databinding.ActivityMainBinding
import com.mikeschvedov.grocerylistms.model.GroceryListItem
import java.lang.ref.WeakReference

class GLActivity : AppCompatActivity(), GroceryListRecyclerViewAdapter.GroceryItemInterface {

    private val viewModel: GLViewModel by lazy {
        ViewModelProvider(this)[GLViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        // Creating an instance of the adapter
        val grocerylistAdapter = GroceryListRecyclerViewAdapter(WeakReference(this))
        // Binding the adapter to the recyclerview
        binding.recyclerView.adapter = grocerylistAdapter
        // Getting the data from the databse
        viewModel.fetchGroceryData()

        // Clicking on the Add New Item Button
        binding.floatingButtonAddBTN.setOnClickListener {
            addBTNPressed(binding)
        }

        // Clicking on the Cancel Adding New Item
        binding.floatingButtonCancelBTN.setOnClickListener {
            cancelBTNPressed(binding)
        }

        binding.addButton.setOnClickListener {

            val itemName = binding.itemNameInputfield.text.toString()
            val itemAmount = binding.itemAmountInputfield.text.toString()

            println("INFO FROM INPUT FIELDS: ITEM NAME: ${itemName}  ITEM AMOUNT: ${itemAmount}")

            if (itemName.isBlank()){
                // If the user did not enter something into the item name input field (amount field is not mandatory)
                Toast.makeText(this, "חסר מידע!", Toast.LENGTH_SHORT).show()
            }else{

                val newID: String = viewModel.generateNewID()

                val newItem: GroceryListItem = GroceryListItem(newID, itemName, itemAmount, false)
                viewModel.addNewItemToDatabase(newItem)
                cancelBTNPressed(binding)
            }

        }



    }

    fun addBTNPressed(binding: ActivityMainBinding) {
        binding.addItemLayout.isVisible = true
        binding.floatingButtonAddBTN.isVisible = false
        binding.floatingButtonCancelBTN.isVisible = true
    }

    fun cancelBTNPressed(binding: ActivityMainBinding) {
        binding.addItemLayout.isVisible = false
        binding.floatingButtonAddBTN.isVisible = true
        binding.floatingButtonCancelBTN.isVisible = false
    }




    // region GroceryListRecyclerViewAdapter.GroceryItemInterface ---------------//

    // Implementing the required function from the RecyclerAdapter's Interface
    override fun onGroceryItemClicked(url: String) {
        /* val intent = Intent(this, Activinity::class.java).apply {
             putExtra(Activity, url)
         }
         startActivity(intent)*/
    }

    override fun onIsBoughtStatusChanged(groceryItemId: String, newStatus: Boolean) {
        viewModel.updateIsItemBought(groceryItemId, newStatus)
    }


    //endregion GroceryListRecyclerViewAdapter.GroceryItemInterface --------------//

}

