package com.mikeschvedov.grocerylistms

import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mikeschvedov.grocerylistms.databinding.ActivityMainBinding
import com.mikeschvedov.grocerylistms.grocerylist.GroceryListRecyclerViewAdapter
import com.mikeschvedov.grocerylistms.model.GroceryListItem

@BindingAdapter("setItems")
fun setItems(recyclerView: RecyclerView, list: List<GroceryListItem>?){
    (recyclerView.adapter as GroceryListRecyclerViewAdapter).setItems(list)

}
