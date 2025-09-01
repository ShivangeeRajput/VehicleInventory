package com.example.tummocassignment.ui.addvehicle

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
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

    private val brands by lazy {
        listOf(
            OptionAdapter.OptionItem(getString(R.string.brand_tata), R.drawable.ic_tata),
            OptionAdapter.OptionItem(getString(R.string.brand_honda), R.drawable.honda),
            OptionAdapter.OptionItem(getString(R.string.brand_hero), R.drawable.ic_hero),
            OptionAdapter.OptionItem(getString(R.string.brand_bajaj), R.drawable.ic_bajaj),
            OptionAdapter.OptionItem(getString(R.string.brand_yamaha), R.drawable.ic_yamaha),
            OptionAdapter.OptionItem(getString(R.string.brand_other), R.drawable.ic_car)
        )
    }

    private val models by lazy {
        listOf(
            OptionAdapter.OptionItem(getString(R.string.model_activa_4g)),
            OptionAdapter.OptionItem(getString(R.string.model_activa_5g)),
            OptionAdapter.OptionItem(getString(R.string.model_activa_6g)),
            OptionAdapter.OptionItem(getString(R.string.model_activa_125)),
            OptionAdapter.OptionItem(getString(R.string.model_activa_125_bs6)),
            OptionAdapter.OptionItem(getString(R.string.model_activa_h_smart))
        )
    }

    private val fuelTypes by lazy {
        listOf(
            OptionAdapter.OptionItem(getString(R.string.fuel_petrol)),
            OptionAdapter.OptionItem(getString(R.string.fuel_electric)),
            OptionAdapter.OptionItem(getString(R.string.fuel_diesel)),
            OptionAdapter.OptionItem(getString(R.string.fuel_cng))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddVehicleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropdowns()
        setupDropdownIcons()
        binding.inputVehicleNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        setupInputValidation()
        updateButtonState()

        binding.btnAddVehicle.setOnClickListener {
            val brand = binding.dropdownBrand.text.toString().trim()
            val model = binding.dropdownModel.text.toString().trim()
            val fuelType = binding.dropdownFuelType.text.toString().trim()
            val number = binding.inputVehicleNumber.text.toString().trim()
            val yearText = binding.dropdownYear.text.toString().trim()

            if (brand.isEmpty() || model.isEmpty() || fuelType.isEmpty() || number.isEmpty() || yearText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveVehicle()
        }

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupDropdowns() {
        binding.dropdownBrand.setOnClickListener { showBrandSelection() }
        binding.dropdownModel.setOnClickListener { showModelSelection() }
        binding.dropdownFuelType.setOnClickListener { showFuelTypeSelection() }
    }

    private fun setupDropdownIcons() {
        binding.brandInputLayout.setEndIconOnClickListener { showBrandSelection() }
        binding.modelInputLayout.setEndIconOnClickListener { showModelSelection() }
        binding.fuelInputLayout.setEndIconOnClickListener { showFuelTypeSelection() }
    }

    private fun showBrandSelection() {
        SelectOptionBottomSheet(
            title = getString(R.string.select_brand_title),
            options = brands
        ) { selected ->
            binding.dropdownBrand.setText(selected)
            binding.brandInputLayout.isHintAnimationEnabled = true
            updateButtonState()
        }.show(parentFragmentManager, "brandSheet")
    }

    private fun showModelSelection() {
        SelectOptionBottomSheet(
            title = getString(R.string.select_model_title),
            options = models
        ) { selected ->
            binding.dropdownModel.setText(selected)
            binding.modelInputLayout.isHintAnimationEnabled = true
            updateButtonState()
        }.show(parentFragmentManager, "modelSheet")
    }

    private fun showFuelTypeSelection() {
        SelectOptionBottomSheet(
            title = getString(R.string.select_fuel_title),
            options = fuelTypes
        ) { selected ->
            binding.dropdownFuelType.setText(selected)
            binding.fuelInputLayout.isHintAnimationEnabled = true
            updateButtonState()
        }.show(parentFragmentManager, "fuelTypeSheet")
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
            Toast.makeText(requireContext(), getString(R.string.error_fill_all_fields), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), getString(R.string.vehicle_added_success), Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), getString(R.string.vehicle_add_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupInputValidation() {
        val textWatcher = { _: CharSequence?, _: Int, _: Int, _: Int -> updateButtonState() }

        binding.inputVehicleNumber.doOnTextChanged(textWatcher)
        binding.inputOwnerName.doOnTextChanged(textWatcher)
        binding.dropdownYear.doOnTextChanged(textWatcher)
        binding.dropdownBrand.doOnTextChanged(textWatcher)
        binding.dropdownModel.doOnTextChanged(textWatcher)
        binding.dropdownFuelType.doOnTextChanged(textWatcher)
    }

    private fun updateButtonState() {
        val brand = binding.dropdownBrand.text.toString().trim()
        val model = binding.dropdownModel.text.toString().trim()
        val fuelType = binding.dropdownFuelType.text.toString().trim()
        val number = binding.inputVehicleNumber.text.toString().trim()
        val year = binding.dropdownYear.text.toString().trim()
        val ownerName = binding.inputOwnerName.text.toString().trim()

        val enabled = brand.isNotEmpty() && model.isNotEmpty() && fuelType.isNotEmpty()
                && number.isNotEmpty() && year.isNotEmpty() && ownerName.isNotEmpty()

        binding.btnAddVehicle.isEnabled = enabled

        if (enabled) {
            binding.btnAddVehicle.setBackgroundResource(R.drawable.bg_btn_enabled)
        } else {
            binding.btnAddVehicle.setBackgroundResource(R.drawable.bg_btn_disabled)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
