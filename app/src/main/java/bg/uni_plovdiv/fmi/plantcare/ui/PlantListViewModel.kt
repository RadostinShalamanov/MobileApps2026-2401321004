package bg.uni_plovdiv.fmi.plantcare.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import bg.uni_plovdiv.fmi.plantcare.data.Plant
import bg.uni_plovdiv.fmi.plantcare.data.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
    repository: PlantRepository
) : ViewModel() {

    val plants: LiveData<List<Plant>> = repository.plants.asLiveData()
}