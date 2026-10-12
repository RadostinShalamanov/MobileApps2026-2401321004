package bg.uni_plovdiv.fmi.plantcare.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val species: String = "",
    val lastWateredDay: Long,
    val intervalDays: Int,
    val photoPath: String? = null
)