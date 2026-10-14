package bg.uni_plovdiv.fmi.plantcare.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import bg.uni_plovdiv.fmi.plantcare.R
import bg.uni_plovdiv.fmi.plantcare.databinding.FragmentPlantEditBinding
import bg.uni_plovdiv.fmi.plantcare.domain.PlantValidation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantEditFragment : Fragment(R.layout.fragment_plant_edit) {

    private var _binding: FragmentPlantEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlantEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPlantEditBinding.bind(view)

        viewModel.saved.observe(viewLifecycleOwner) { saved ->
            if (saved) findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val name = binding.etName.text?.toString().orEmpty()
        val interval = PlantValidation.parseInterval(binding.etInterval.text?.toString())

        binding.tilName.error =
            if (PlantValidation.isValidName(name)) null else getString(R.string.error_name)
        binding.tilInterval.error =
            if (interval != null) null else getString(R.string.error_interval)

        if (PlantValidation.isValidName(name) && interval != null) {
            viewModel.save(name, binding.etSpecies.text?.toString().orEmpty(), interval)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}