package bg.uni_plovdiv.fmi.plantcare.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bg.uni_plovdiv.fmi.plantcare.data.Plant
import bg.uni_plovdiv.fmi.plantcare.data.PlantRepository
import bg.uni_plovdiv.fmi.plantcare.domain.WateringCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
    private val repository: PlantRepository
) : ViewModel() {

    val plants: LiveData<List<Plant>> = repository.plants.asLiveData()

    fun water(plant: Plant) = viewModelScope.launch {
        repository.markWatered(plant, WateringCalculator.todayEpochDay())
    }

    fun delete(plant: Plant) = viewModelScope.launch { repository.delete(plant) }

    fun restore(plant: Plant) = viewModelScope.launch { repository.restore(plant) }
}