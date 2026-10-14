package bg.uni_plovdiv.fmi.plantcare.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bg.uni_plovdiv.fmi.plantcare.R
import bg.uni_plovdiv.fmi.plantcare.databinding.FragmentPlantListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantListFragment : Fragment(R.layout.fragment_plant_list) {

    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlantListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPlantListBinding.bind(view)

        val adapter = PlantAdapter()
        binding.rvPlants.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlants.adapter = adapter

        viewModel.plants.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_edit)
        }
    }

    override fun onDestroyView() {
        binding.rvPlants.adapter = null
        _binding = null
        super.onDestroyView()
    }
}