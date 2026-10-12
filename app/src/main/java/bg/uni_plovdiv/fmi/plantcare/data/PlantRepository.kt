package bg.uni_plovdiv.fmi.plantcare.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepository @Inject constructor(private val dao: PlantDao) {

    val plants: Flow<List<Plant>> = dao.observeAll()

    suspend fun get(id: Long): Plant? = dao.getById(id)

    suspend fun add(plant: Plant): Long = dao.insert(plant)

    suspend fun update(plant: Plant) = dao.update(plant)

    suspend fun delete(plant: Plant) = dao.delete(plant)

    suspend fun restore(plant: Plant) {
        dao.insert(plant)
    }

    suspend fun markWatered(plant: Plant, today: Long) {
        dao.update(plant.copy(lastWateredDay = today))
    }
}