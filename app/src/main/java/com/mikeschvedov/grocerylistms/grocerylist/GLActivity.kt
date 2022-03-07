package com.mikeschvedov.grocerylistms.grocerylist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
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

