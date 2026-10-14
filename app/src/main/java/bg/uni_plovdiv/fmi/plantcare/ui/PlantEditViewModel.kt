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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PlantEditViewModel @Inject constructor(
    private val repository: PlantRepository
) : ViewModel() {

    private val _plant = MutableLiveData<Plant?>()
    val plant: LiveData<Plant?> = _plant

    private val _photoPath = MutableLiveData<String?>()
    val photoPath: LiveData<String?> = _photoPath

    private val _saved = MutableLiveData(false)
    val saved: LiveData<Boolean> = _saved

    /** Пътят на снимката, която камерата в момента записва. */
    var pendingPhotoPath: String? = null

    /** Попълнена ли е формата от базата (за да не се презаписва при завъртане). */
    var formPopulated = false

    private var loaded = false

    fun load(id: Long) {
        if (loaded || id == 0L) return
        loaded = true
        viewModelScope.launch {
            repository.get(id)?.let {
                _plant.value = it
                _photoPath.value = it.photoPath
            }
        }
    }

    fun onPictureResult(success: Boolean) {
        val path = pendingPhotoPath
        if (success) _photoPath.value = path else path?.let { File(it).delete() }
        pendingPhotoPath = null
    }

    fun save(name: String, species: String, intervalDays: Int) {
        val existing = _plant.value
        viewModelScope.launch {
            if (existing == null) {
                repository.add(
                    Plant(
                        name = name.trim(),
                        species = species.trim(),
                        lastWateredDay = WateringCalculator.todayEpochDay(),
                        intervalDays = intervalDays,
                        photoPath = _photoPath.value
                    )
                )
            } else {
                repository.update(
                    existing.copy(
                        name = name.trim(),
                        species = species.trim(),
                        intervalDays = intervalDays,
                        photoPath = _photoPath.value
                    )
                )
            }
            _saved.value = true
        }
    }
}