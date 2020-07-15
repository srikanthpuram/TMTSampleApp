package com.srikanthpuram.tmtsampleapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.srikanthpuram.tmtsampleapp.R
import com.srikanthpuram.tmtsampleapp.adapter.TmtAdapter
import com.srikanthpuram.tmtsampleapp.databinding.ActivityHomeBinding
import com.srikanthpuram.tmtsampleapp.repository.TmtRepository
import com.srikanthpuram.tmtsampleapp.util.Resource
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: TmtViewModel
    private lateinit var tmtAdapter: TmtAdapter
    private lateinit var binding: ActivityHomeBinding

    private var isLoading = false

    private val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //create repository and viewmodel instances
        val tmtRepository = TmtRepository()
        val viewModelProviderFactory = TmtViewModelProviderFactory(application, tmtRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(TmtViewModel::class.java)

        //setup the recyclerview tio display the list
        setupRecyclerView()

        //observe for the LiveData received from ViewModel
        viewModel.imageList.observe(this, Observer { response ->
            when(response) {
                //handle success scenario, pass the data to the adapter
                is Resource.Success -> {
                    Log.d(TAG, "SUCCESS")
                    tvNoData.visibility = View.GONE
                    hideProgressBar()
                    response.data?.let {  receivedData ->
                        val cards = receivedData.page.cards.toList()
                        tmtAdapter.differ.submitList(cards)
                    }
                }

                //error scenario, show error toast
                is Resource.Error -> {
                    Log.d(TAG, "ERROR")
                    tvNoData.visibility = View.VISIBLE
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(this, "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                }

                //show the loading progress bar, when network operation is in progress
                is Resource.Loading -> {
                    Log.d(TAG, "LOADING")
                    tvNoData.visibility = View.GONE
                    showProgressBar()
                }
            }
        })
    }

    /**
     * function to setup the recyclerview
     */
    private fun setupRecyclerView() {
        tmtAdapter = TmtAdapter()
        rvTmtCards.apply {
            addItemDecoration(DividerItemDecoration(this@HomeActivity, DividerItemDecoration.HORIZONTAL))
            adapter = tmtAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

    /**
     * hide the progressbar
     */
    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    /**
     * show the progressbar
     */
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }
}
