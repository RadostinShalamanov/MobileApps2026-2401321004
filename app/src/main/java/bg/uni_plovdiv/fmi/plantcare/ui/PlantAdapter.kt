package bg.uni_plovdiv.fmi.plantcare.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bg.uni_plovdiv.fmi.plantcare.R
import bg.uni_plovdiv.fmi.plantcare.data.Plant
import bg.uni_plovdiv.fmi.plantcare.databinding.ItemPlantBinding
import bg.uni_plovdiv.fmi.plantcare.domain.WateringCalculator
import bg.uni_plovdiv.fmi.plantcare.util.ImageUtils

class PlantAdapter(
    private val onClick: (Plant) -> Unit,
    private val onWater: (Plant) -> Unit
) : ListAdapter<Plant, PlantAdapter.Holder>(DIFF) {

    class Holder(val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val plant = getItem(position)
        val b = holder.binding
        val ctx = b.root.context

        b.itemName.text = plant.name
        b.itemSpecies.text = plant.species.ifBlank { ctx.getString(R.string.unknown_species) }

        val days = WateringCalculator.daysUntilWatering(
            plant.lastWateredDay, plant.intervalDays, WateringCalculator.todayEpochDay()
        ).toInt()
        b.itemStatus.text = when {
            days == 0 -> ctx.getString(R.string.status_today)
            days < 0 -> ctx.getString(R.string.status_overdue, -days)
            else -> ctx.getString(R.string.status_in, days)
        }

        val bitmap = plant.photoPath?.let { ImageUtils.decodeScaled(it, 160) }
        if (bitmap != null) b.itemPhoto.setImageBitmap(bitmap)
        else b.itemPhoto.setImageResource(android.R.drawable.ic_menu_gallery)

        b.root.setOnClickListener { onClick(plant) }
        b.btnWater.setOnClickListener { onWater(plant) }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Plant>() {
            override fun areItemsTheSame(a: Plant, b: Plant) = a.id == b.id
            override fun areContentsTheSame(a: Plant, b: Plant) = a == b
        }
    }
}