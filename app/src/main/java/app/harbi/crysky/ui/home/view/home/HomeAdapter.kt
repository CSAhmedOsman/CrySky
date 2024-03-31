package app.harbi.crysky.ui.home.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.harbi.crysky.R
import app.harbi.crysky.databinding.ItemDayBinding
import app.harbi.crysky.databinding.ItemHourBinding
import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.data.WeatherData
import app.harbi.crysky.model.loadImage

class HourAdapter(val getString: (resId: Int) -> String) :
    ListAdapter<WeatherData, ViewHolder<ItemHourBinding>>(WeatherDataDiffUtil()) {

    private lateinit var binding: ItemHourBinding
    var units = Constants.METRIC

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ItemHourBinding> {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = ItemHourBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemHourBinding>, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            tvTime.text = item.dt_txt.split(" ")[0]
            tvHour.text = item.dt_txt.split(" ")[1]
            loadImage(ivStatus, item.weather[0].icon, R.drawable.d02)
            tvDegree.text =
                String.format(
                    "%.2f %s", item.main.feels_like, when (units) {
                        Constants.STANDARD -> getString(R.string.kelvin)
                        Constants.IMPERIAL -> getString(R.string.fahrenheit)
                        else -> getString(R.string.celsius)
                    }
                )

        }
    }
}

class DayAdapter(val getString: (resId: Int) -> String) :
    ListAdapter<WeatherData, ViewHolder<ItemDayBinding>>(WeatherDataDiffUtil()) {

    private lateinit var binding: ItemDayBinding
    var units = Constants.METRIC

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ItemDayBinding> {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = ItemDayBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemDayBinding>, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            tvTime.text = item.dt_txt.split(" ")[0]
            tvDescription.text = item.weather[0].description
            loadImage(ivStatus, item.weather[0].icon, R.drawable.d02)
            tvDegree.text =
                String.format(
                    "%.2f / %.2f %s", item.main.temp_max, item.main.temp_min, when (units) {
                        Constants.STANDARD -> getString(R.string.kelvin)
                        Constants.IMPERIAL -> getString(R.string.fahrenheit)
                        else -> getString(R.string.celsius)
                    }
                )
        }
    }
}

class ViewHolder<T : ViewBinding>(var binding: T) : RecyclerView.ViewHolder(binding.root)

class WeatherDataDiffUtil : DiffUtil.ItemCallback<WeatherData>() {
    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem == newItem
    }
}

