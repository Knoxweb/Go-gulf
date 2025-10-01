package com.gogulf.passenger.app.ui.locationselector

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityLocationSelectorBinding

class LocationSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationSelectorBinding
    private lateinit var viewModel: LocationSelectorViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        SystemBarUtil.enableEdgeToEdge(this)
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLocationSelectorBinding>(
            this, R.layout.activity_location_selector
        ).also {
            binding = it
            viewModel = ViewModelProvider(this)[LocationSelectorViewModel::class.java]
            binding.viewModel = viewModel.also {
                val searchQueryCallBack = object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        viewModel.updateSearchQuery()
                    }
                }

                viewModel.searchQuery.addOnPropertyChangedCallback(searchQueryCallBack)
            }

        }

        binding.recyclerGoogleAutocomplete.layoutManager = LinearLayoutManager(this)

        viewModel.googleAutoCompleteAdapter.observe(this) {
            binding.recyclerGoogleAutocomplete.adapter = it
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        viewModel.uiState.asLiveData().observe(this) {
            if (it.selectedAddress.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("data", it)
                setResult(RESULT_OK, intent)
                finish()
            }

        }

    }
}