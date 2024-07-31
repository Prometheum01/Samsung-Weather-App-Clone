package com.rurouni.weatherapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rurouni.weatherapp.R
import com.rurouni.weatherapp.ViewModel.DetailViewModel
import com.rurouni.weatherapp.ViewModel.DetailViewModelFactory
import com.rurouni.weatherapp.ViewModel.MainPageViewModel
import com.rurouni.weatherapp.ViewModel.MainPageViewModelFactory
import com.rurouni.weatherapp.adapter.DetailAdapter
import com.rurouni.weatherapp.adapter.ForecastListAdapter
import com.rurouni.weatherapp.databinding.FragmentDetailViewBinding
import com.rurouni.weatherapp.service.WeatherApi
import com.rurouni.weatherapp.service.WeatherRepository

class DetailView : Fragment() {
    private lateinit var binding : FragmentDetailViewBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var adapter : DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val weatherApi = WeatherApi.getInstance()
        val weatherRepository = WeatherRepository(weatherApi)
        viewModel = ViewModelProvider(this, DetailViewModelFactory(weatherRepository)).get(
            DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailViewBinding.inflate(inflater, container, false)

        adapter = DetailAdapter()
        binding.detailRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.detailRecyclerView.adapter = adapter

        viewModel.weatherModel.observe(viewLifecycleOwner) { adapter.setModel(it) }

        viewModel.errorMessage.observe(viewLifecycleOwner) { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.detailLoadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        getDetails()

        return binding.root
    }

    private fun getDetails() {
        arguments?.let {
            viewModel.getData(DetailViewArgs.fromBundle(it).location, DetailViewArgs.fromBundle(it).date)
        }
    }

}