package bg.uni_plovdiv.fmi.plantcare.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import bg.uni_plovdiv.fmi.plantcare.R
import bg.uni_plovdiv.fmi.plantcare.databinding.FragmentPlantEditBinding
import bg.uni_plovdiv.fmi.plantcare.domain.PlantValidation
import bg.uni_plovdiv.fmi.plantcare.util.ImageUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantEditFragment : Fragment(R.layout.fragment_plant_edit) {

    private var _binding: FragmentPlantEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlantEditViewModel by viewModels()

    // Camera Intent: системната камера записва снимката във FileProvider URI.
    // Не е нужно CAMERA разрешение, защото приложението не отваря камерата директно.
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            viewModel.onPictureResult(success)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPlantEditBinding.bind(view)

        val plantId = arguments?.getLong("plantId") ?: 0L
        viewModel.load(plantId)

        viewModel.plant.observe(viewLifecycleOwner) { plant ->
            if (plant != null && !viewModel.formPopulated) {
                viewModel.formPopulated = true
                binding.etName.setText(plant.name)
                binding.etSpecies.setText(plant.species)
                binding.etInterval.setText(plant.intervalDays.toString())
            }
        }

        viewModel.photoPath.observe(viewLifecycleOwner) { path ->
            val bitmap = path?.let { ImageUtils.decodeScaled(it, 800) }
            if (bitmap != null) binding.ivPhoto.setImageBitmap(bitmap)
            else binding.ivPhoto.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        viewModel.saved.observe(viewLifecycleOwner) { saved ->
            if (saved) findNavController().popBackStack()
        }

        binding.btnPhoto.setOnClickListener { launchCamera() }
        binding.btnSave.setOnClickListener { save() }
    }

    private fun launchCamera() {
        val ctx = requireContext()
        val file = ImageUtils.createImageFile(ctx)
        viewModel.pendingPhotoPath = file.absolutePath
        val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
        takePicture.launch(uri)
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