package com.example.tummocassignment.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tummocassignment.R
import com.example.tummocassignment.databinding.FragmentHomeBinding
import com.example.tummocassignment.domain.model.Vehicle
import com.example.tummocassignment.domain.repository.VehicleRepository
import com.example.tummocassignment.ui.home.adapter.VehicleAdapter
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

        adapter.setOnVehicleLongClickListener { vehicle ->
            showVehicleOptionsDialog(vehicle)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.vehicles.collect { vehicles ->
                adapter.submitList(vehicles)
                updateUI(vehicles)
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
                    updateUI(filteredVehicles)
                }
            )
            filterSheet.show(parentFragmentManager, "FilterBottomSheet")
        }
    }

    private fun updateUI(vehicles: List<Vehicle>) {
        binding.tvTotalVehicles.text = vehicles.size.toString()
        val totalEv = vehicles.count { it.fuelType.equals("Electric", true) }
        binding.tvTotalEv.text = totalEv.toString()
        binding.NoVehicles.visibility = if (vehicles.isEmpty()) View.VISIBLE else View.GONE
        binding.rvVehicles.visibility = if (vehicles.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun showVehicleOptionsDialog(vehicle: Vehicle) {
        val options = arrayOf("Update", "Delete")

        AlertDialog.Builder(requireContext())
            .setTitle("Choose an option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> navigateToUpdateVehicle(vehicle)
                    1 -> deleteVehicle(vehicle)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun navigateToUpdateVehicle(vehicle: Vehicle) {
        val bundle = bundleOf("vehicle" to vehicle)
        findNavController().navigate(R.id.action_homeFragment_to_addVehicleFragment, bundle)
    }

    private fun deleteVehicle(vehicle: Vehicle) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Vehicle")
            .setMessage("Are you sure you want to delete ${vehicle.brand} ${vehicle.model}?")
            .setPositiveButton("Delete") { dialog, which ->
                viewLifecycleOwner.lifecycleScope.launch {
                    vehicle.id?.let { viewModel.deleteVehicle(it) }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



