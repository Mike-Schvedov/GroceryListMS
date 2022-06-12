package com.mikeschvedov.grocerylistms.grocerylist


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.mikeschvedov.grocerylistms.R
import com.mikeschvedov.grocerylistms.databinding.ActivityMainBinding

import java.lang.ref.WeakReference


const val NOTIFICATION_TOPIC = "/topics/myTopic"

class GLActivity : AppCompatActivity(), GroceryListRecyclerViewAdapter.GroceryItemInterface {

    /* Version 1.1 Commit 5 at 29/5/22 10:20  */

    private val viewModel: GLViewModel by lazy {
        ViewModelProvider(this)[GLViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)


        // Subscribe app to topic
        viewModel.subscribeDeviceToTopic()

        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        // Creating an instance of the adapter
        val grocerylistAdapter = GroceryListRecyclerViewAdapter(WeakReference(this))

        // So the list will not "shake" when reloading data
        grocerylistAdapter.setHasStableIds(true)

        // Binding the adapter to the recyclerview
        binding.recyclerView.adapter = grocerylistAdapter

        // Getting the data from the databse
        viewModel.fetchGroceryData()

        // ------------------------------- TOOLBAR MENU --------------------------- //

        var toolbarIsOpen: Boolean = false

        // Clicking on toolbar menu (we access the include tag)
        binding.includeToolbar.toolbarmenuButton.setOnClickListener {

            if (!toolbarIsOpen) { // if menu is closed
                binding.includeToolbar.toolbarmenuLayout.isVisible = true
                toolbarIsOpen = true
            } else { // if menu is open
                binding.includeToolbar.toolbarmenuLayout.isVisible = false
                toolbarIsOpen = false
            }

        }
        /*                       CLICKING ON THE DELETE "SELECTED ONLY" BUTTON                      */

        binding.includeToolbar.deleteOnlySelectedBTN.setOnClickListener {

            viewModel.openingAlertSelectedLayout(binding)

            toolbarIsOpen = false


            // ------------- CLICKING ON THE "DELETE" BUTTON ----------------- //
            binding.includeToolbar.alertSelectedAcceptBtn.setOnClickListener {

                viewModel.closingAlertSelectedLayout(binding)

                // execute deletion
                viewModel.deleteOnlySelected()

            }
            // ------------- CLICKING ON THE "CANCEL"" BUTTON ----------------- //
            binding.includeToolbar.alertSelectedDeclineBtn.setOnClickListener {

                viewModel.closingAlertSelectedLayout(binding)

            }


        }

        /*                       CLICKING ON THE "DELETE ENTIRE LIST" BUTTON                      */

        binding.includeToolbar.deleteEntireListBTN.setOnClickListener {

            viewModel.openingAlertAllLayout(binding)
            toolbarIsOpen = false

            // ------------- CLICKING ON THE "DELETE" BUTTON ----------------- //
            binding.includeToolbar.alertAllAcceptBtn.setOnClickListener {

                // closing the alert all layout and re-enabling buttons
                viewModel.closingAlertAllLayout(binding)

                // execute deletion
                viewModel.deleteEntireList()

            }
            // ------------- CLICKING ON THE "CANCEL"" BUTTON ----------------- //
            binding.includeToolbar.alertAllDeclineBtn.setOnClickListener {

                // closing the alert all layout and re-enabling buttons
                viewModel.closingAlertAllLayout(binding)

            }


        }


        /*                                 NEW ITEM LAYOUT                                        */

        // clicking on "add new item" floating button
        binding.floatingButtonAddBTN.setOnClickListener {
            viewModel.floatingAddButtonPressed(binding)
        }

        // clicking on the "cancel" floating button
        binding.floatingButtonCancelBTN.setOnClickListener {
            viewModel.floatingCancelButtonNPressed(binding)
        }

        // clicking on "add" (inside layout)
        binding.addButton.setOnClickListener {
            viewModel.clickingAddNewItem(binding, this)
            // ON ADDING AN ITEM SEND NOTIFICATION

        }


    }


    // INTERFACE IMPLEMENTATION

    override fun onGroceryItemClicked(url: String) {
        TODO("Not yet implemented")
    }

    override fun onIsBoughtStatusChanged(groceryItemId: String, newStatus: Boolean) {
        viewModel.updateIsItemBought(groceryItemId, newStatus)
    }





}

