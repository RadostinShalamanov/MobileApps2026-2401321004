package bg.uni_plovdiv.fmi.plantcare.ui

import android.content.Intent
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


    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            viewModel.onPictureResult(success)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPlantEditBinding.bind(view)

        val plantId = arguments?.getLong("plantId") ?: 0L
        viewModel.load(plantId)


        binding.btnShare.visibility = if (plantId == 0L) View.GONE else View.VISIBLE

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
        binding.btnShare.setOnClickListener { share() }
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
    
    private fun share() {
        val interval = PlantValidation.parseInterval(binding.etInterval.text?.toString()) ?: 7
        val species = binding.etSpecies.text?.toString().orEmpty()
            .ifBlank { getString(R.string.unknown_species) }
        val text = getString(
            R.string.share_text,
            binding.etName.text?.toString().orEmpty(),
            species,
            interval
        )
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(send, getString(R.string.share_chooser)))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}