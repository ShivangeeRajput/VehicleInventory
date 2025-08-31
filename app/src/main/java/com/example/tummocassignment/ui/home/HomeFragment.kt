package com.example.tummocassignment.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tummocassignment.R
import com.example.tummocassignment.databinding.FragmentHomeBinding
import com.example.tummocassignment.domain.repository.VehicleRepository
import com.example.tummocassignment.ui.addvehicle.adapter.VehicleAdapter
import com.example.tummocassignment.ui.home.filter.FilterBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: VehicleAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VehicleAdapter()
        binding.rvVehicles.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVehicles.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.vehicles.collect { vehicles ->
                adapter.submitList(vehicles)
                binding.tvTotalVehicles.text = vehicles.size.toString()
                val totalEv = vehicles.count { it.fuelType.equals("Electric", true) }
                binding.tvTotalEv.text = totalEv.toString()
            }
        }

        binding.fabAddVehicle.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addVehicleFragment)
        }

        binding.btnFilter.setOnClickListener {
            val filterSheet = FilterBottomSheet(
                vehicles = viewModel.vehicles.value,
                onFiltered = { filteredVehicles ->
                    adapter.submitList(filteredVehicles)
                    binding.tvTotalVehicles.text = filteredVehicles.size.toString()
                    val totalEv = filteredVehicles.count { it.fuelType.equals("Electric", true) }
                    binding.tvTotalEv.text = totalEv.toString()
                    binding.tvNoVehiclesFound.visibility = if(filteredVehicles.isEmpty()) View.VISIBLE else View.GONE
                }
            )
            filterSheet.show(parentFragmentManager, "FilterBottomSheet")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


