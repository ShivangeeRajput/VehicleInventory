package com.example.tummocassignment.ui.addvehicle.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tummocassignment.R
import com.example.tummocassignment.databinding.BottomsheetSelectOptionBinding
import com.example.tummocassignment.ui.addvehicle.bottomsheet.adapter.OptionAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectOptionBottomSheet(
    private val title: String,
    private val options: List<OptionAdapter.OptionItem>,
    private val onItemSelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetSelectOptionBinding? = null
    private val binding get() = _binding!!
    private var selectedOption: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetSelectOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true

                it.setBackgroundResource(R.drawable.bg_bottomsheet_rounded)
            }
        }

        binding.tvTitle.text = title
        binding.ivCloseFilter.setOnClickListener { dismiss() }

        val adapter = OptionAdapter(options) { selected ->
            selectedOption = selected.name
        }

        binding.recyclerViewOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewOptions.adapter = adapter

        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
            divider.setDrawable(it)
        }
        binding.recyclerViewOptions.addItemDecoration(divider)
        binding.btnApply.setOnClickListener {
            selectedOption?.let { selected ->
                onItemSelected(selected)
                dismiss()
            } ?: run {
                Toast.makeText(requireContext(), "Please select an option", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.drawable.bg_bottomsheet_rounded)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



