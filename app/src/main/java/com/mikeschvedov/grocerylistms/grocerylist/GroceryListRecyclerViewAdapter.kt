package com.mikeschvedov.grocerylistms.grocerylist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikeschvedov.grocerylistms.R
import com.mikeschvedov.grocerylistms.databinding.ViewHolderGrocerylistItemBinding
import com.mikeschvedov.grocerylistms.model.GroceryListItem
import java.lang.ref.WeakReference

class GroceryListRecyclerViewAdapter(
    private val callbackWeakRef: WeakReference<GroceryItemInterface>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface GroceryItemInterface {
        fun onGroceryItemClicked(url: String)
        fun onIsBoughtStatusChanged(groceryItemId: String, newStatus: Boolean)
    }


    private val groceryItemsList = mutableListOf<GroceryListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // For each position, we are creating a view holder
        return GroceryItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // We bind the data from the list to the viewholder
        (holder as GroceryItemViewHolder).onBind(
            grocerylistItem = groceryItemsList[position],
            onClick = {url ->
                callbackWeakRef.get()?.onGroceryItemClicked(url)
            },
            onIsBoughtChanged = {newStatus ->
                callbackWeakRef.get()?.onIsBoughtStatusChanged(groceryItemsList[position].id, newStatus)
            }
        )
    }

    override fun getItemCount(): Int {
        // Tell the adapter how many views to create
        return groceryItemsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(groceryItemsList: List<GroceryListItem>?) {
        this.groceryItemsList.clear()
        this.groceryItemsList.addAll(groceryItemsList ?: emptyList())
        notifyDataSetChanged()
    }

    inner class GroceryItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_grocerylist_item, parent, false)
    ) {
        private val binding = ViewHolderGrocerylistItemBinding.bind((itemView))

        fun onBind(
            grocerylistItem: GroceryListItem,
            onClick: (String) -> Unit,
            onIsBoughtChanged: (Boolean) -> Unit
        ) {
            binding.title = grocerylistItem.title
            binding.description = grocerylistItem.description

            binding.root.setOnClickListener {
                onClick(grocerylistItem.title)
            }

            // Toggling the "isBought" checkbox
            val drawableResId: Int = if (grocerylistItem.bought) {
                R.drawable.checkbox_ckecked
            } else {
                R.drawable.checkbox_empty
            }
            binding.isBoughtItemview.setImageResource(drawableResId)
            binding.isBoughtItemview.setOnClickListener {
                val newStatus = !grocerylistItem.bought
                //to bubble up the change
                onIsBoughtChanged(newStatus)
                println("click -------------------- to status ${newStatus}")
            }


        }


    }

}