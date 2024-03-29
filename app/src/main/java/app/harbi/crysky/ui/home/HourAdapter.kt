package app.harbi.crysky.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.harbi.crysky.R
import app.harbi.crysky.databinding.ItemHourBinding
import app.harbi.crysky.loadImage
import app.harbi.crysky.model.WeatherData

class HourAdapter : ListAdapter<WeatherData, HourAdapter.ViewHolder>(ProductDiffUtil()) {

    private lateinit var binding: ItemHourBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = ItemHourBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            tvTime.text = item.dt_txt.split(" ")[0]
            tvHour.text = item.dt_txt.split(" ")[1]
            loadImage(ivStatus, item.weather[0].icon, R.drawable.d02)
            tvDegree.text = String.format("%.2f °c", item.main.feels_like)
        }
    }

    class ViewHolder(var binding: ItemHourBinding) : RecyclerView.ViewHolder(binding.root)
}

class ProductDiffUtil : DiffUtil.ItemCallback<WeatherData>() {
    override fun areItemsTheSame(
        oldItem: WeatherData,
        newItem: WeatherData,
    ): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(
        oldItem: WeatherData,
        newItem: WeatherData,
    ): Boolean {
        return oldItem == newItem
    }
}

