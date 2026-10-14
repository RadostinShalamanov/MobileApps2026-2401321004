package bg.uni_plovdiv.fmi.plantcare.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.uni_plovdiv.fmi.plantcare.R
import bg.uni_plovdiv.fmi.plantcare.databinding.FragmentPlantListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantListFragment : Fragment(R.layout.fragment_plant_list) {

    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlantListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPlantListBinding.bind(view)

        val adapter = PlantAdapter(
            onClick = { openEdit(it.id) },
            onWater = { plant ->
                viewModel.water(plant)
                Snackbar.make(
                    binding.root,
                    getString(R.string.watered, plant.name),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        )
        binding.rvPlants.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlants.adapter = adapter

        // Плъзгане настрани = изтриване, със Snackbar за отмяна.
        val swipe = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val plant = adapter.currentList[viewHolder.bindingAdapterPosition]
                viewModel.delete(plant)
                Snackbar.make(binding.root, R.string.deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) { viewModel.restore(plant) }
                    .show()
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(binding.rvPlants)

        viewModel.plants.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAdd.setOnClickListener { openEdit(0L) }
    }

    private fun openEdit(plantId: Long) {
        findNavController().navigate(R.id.action_list_to_edit, bundleOf("plantId" to plantId))
    }

    override fun onDestroyView() {
        binding.rvPlants.adapter = null
        _binding = null
        super.onDestroyView()
    }
}