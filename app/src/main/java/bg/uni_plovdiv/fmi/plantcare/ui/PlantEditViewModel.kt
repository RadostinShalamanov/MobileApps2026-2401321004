package bg.uni_plovdiv.fmi.plantcare.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bg.uni_plovdiv.fmi.plantcare.data.Plant
import bg.uni_plovdiv.fmi.plantcare.data.PlantRepository
import bg.uni_plovdiv.fmi.plantcare.domain.WateringCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantEditViewModel @Inject constructor(
    private val repository: PlantRepository
) : ViewModel() {

    private val _saved = MutableLiveData(false)
    val saved: LiveData<Boolean> = _saved

    fun save(name: String, species: String, intervalDays: Int) {
        viewModelScope.launch {
            repository.add(
                Plant(
                    name = name.trim(),
                    species = species.trim(),
                    lastWateredDay = WateringCalculator.todayEpochDay(),
                    intervalDays = intervalDays
                )
            )
            _saved.value = true
        }
    }
}