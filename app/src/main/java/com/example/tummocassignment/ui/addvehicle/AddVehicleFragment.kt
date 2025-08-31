package com.example.tummocassignment.ui.addvehicle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tummocassignment.R
import com.example.tummocassignment.data.local.entity.VehicleEntity
import com.example.tummocassignment.data.mapper.VehicleMapper
import com.example.tummocassignment.databinding.FragmentAddVehicleBinding
import com.example.tummocassignment.domain.repository.VehicleRepository
import com.example.tummocassignment.ui.addvehicle.bottomsheet.SelectOptionBottomSheet
import com.example.tummocassignment.ui.addvehicle.bottomsheet.adapter.OptionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddVehicleFragment : Fragment() {

    private var _binding: FragmentAddVehicleBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var vehicleRepository: VehicleRepository

    private val brands = listOf(
        OptionAdapter.OptionItem("Tata", R.drawable.ic_tata),
        OptionAdapter.OptionItem("Honda", R.drawable.honda),
        OptionAdapter.OptionItem("Hero", R.drawable.ic_hero),
        OptionAdapter.OptionItem("Bajaj", R.drawable.ic_bajaj),
        OptionAdapter.OptionItem("Yamaha", R.drawable.ic_yamaha),
        OptionAdapter.OptionItem("Other", R.drawable.ic_car),
    )

    private val models = listOf(
        OptionAdapter.OptionItem("Activa 4G"),
        OptionAdapter.OptionItem("Activa 5G"),
        OptionAdapter.OptionItem("Activa 6G"),
        OptionAdapter.OptionItem("Activa 125"),
        OptionAdapter.OptionItem("Activa 125 BS6"),
        OptionAdapter.OptionItem("Activa H-Smart")
    )

    private val fuelTypes = listOf(
        OptionAdapter.OptionItem("Petrol"),
        OptionAdapter.OptionItem("Electric"),
        OptionAdapter.OptionItem("Diesel"),
        OptionAdapter.OptionItem("CNG")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddVehicleBinding.inflate(inflater, container, false)

        setupDropdowns()

        binding.btnAddVehicle.setOnClickListener {
            saveVehicle()
        }

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root
    }

    private fun setupDropdowns() {
        binding.dropdownBrand.setOnClickListener {
            SelectOptionBottomSheet(
                title = "Select Vehicle Brand",
                options = brands
            ) { selected ->
                binding.dropdownBrand.setText(selected)
            }.show(parentFragmentManager, "brandSheet")
        }

        binding.dropdownModel.setOnClickListener {
            SelectOptionBottomSheet(
                title = "Select Vehicle Model",
                options = models
            ) { selected ->
                binding.dropdownModel.setText(selected)
            }.show(parentFragmentManager, "modelSheet")
        }

        binding.dropdownFuelType.setOnClickListener {
            SelectOptionBottomSheet(
                title = "Select Fuel Type",
                options = fuelTypes
            ) { selected ->
                binding.dropdownFuelType.setText(selected)
            }.show(parentFragmentManager, "fuelTypeSheet")
        }
    }

    private fun saveVehicle() {
        val brand = binding.dropdownBrand.text.toString().trim()
        val model = binding.dropdownModel.text.toString().trim()
        val fuelType = binding.dropdownFuelType.text.toString().trim()
        val number = binding.inputVehicleNumber.text.toString().trim()
        val yearText = binding.dropdownYear.text.toString().trim()
        val ownerName = binding.inputOwnerName.text.toString().trim()

        val year = yearText.toIntOrNull()

        if (brand.isBlank() || model.isBlank() || fuelType.isBlank() || number.isBlank() || year == null) {
            Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val entity = VehicleEntity(
                brand = brand,
                model = model,
                fuelType = fuelType,
                vehicleNumber = number,
                yearOfPurchase = year,
                ownerName = ownerName
            )

            val vehicle = VehicleMapper.fromEntity(entity)

            val id = vehicleRepository.addVehicle(vehicle)

            if (id > 0) {
                Toast.makeText(requireContext(), "Vehicle added successfully!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Failed to add vehicle!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





