package bg.uni_plovdiv.fmi.plantcare

import bg.uni_plovdiv.fmi.plantcare.data.Plant
import bg.uni_plovdiv.fmi.plantcare.data.PlantDao
import bg.uni_plovdiv.fmi.plantcare.data.PlantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test


private class FakePlantDao : PlantDao {
    private val items = MutableStateFlow<List<Plant>>(emptyList())
    private var nextId = 1L

    override fun observeAll(): Flow<List<Plant>> = items

    override suspend fun getById(id: Long) = items.value.firstOrNull { it.id == id }

    override suspend fun insert(plant: Plant): Long {
        val id = if (plant.id == 0L) nextId++ else plant.id
        items.value = items.value.filterNot { it.id == id } + plant.copy(id = id)
        return id
    }

    override suspend fun update(plant: Plant) {
        items.value = items.value.map { if (it.id == plant.id) plant else it }
    }

    override suspend fun delete(plant: Plant) {
        items.value = items.value.filterNot { it.id == plant.id }
    }
}

class PlantRepositoryTest {

    private val repo = PlantRepository(FakePlantDao())
    private val sample = Plant(name = "Фикус", lastWateredDay = 100L, intervalDays = 5)

    @Test
    fun add_thenGet_returnsPlant() = runTest {
        val id = repo.add(sample)
        assertEquals("Фикус", repo.get(id)?.name)
    }

    @Test
    fun markWatered_updatesLastWateredDay() = runTest {
        val id = repo.add(sample)
        repo.markWatered(repo.get(id)!!, today = 150L)
        assertEquals(150L, repo.get(id)?.lastWateredDay)
    }

    @Test
    fun delete_removesPlant() = runTest {
        val id = repo.add(sample)
        repo.delete(repo.get(id)!!)
        assertNull(repo.get(id))
    }

    @Test
    fun restore_bringsBackSameId() = runTest {
        val id = repo.add(sample)
        val saved = repo.get(id)!!
        repo.delete(saved)
        repo.restore(saved)
        assertEquals(id, repo.get(id)?.id)
    }
}