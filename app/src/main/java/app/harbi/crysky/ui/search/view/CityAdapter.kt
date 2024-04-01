package app.harbi.crysky.ui.search.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.harbi.crysky.databinding.ItemCityBinding
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.getFlagEmoji
import app.harbi.crysky.ui.home.view.home.ViewHolder

class CityAdapter(
    private val brnSrc: Int,
    private val itemClick: (CityResponse) -> Unit,
    private val addFav: (CityResponse) -> Unit,
    private val goToForward: (CityResponse) -> Unit,
) :
    ListAdapter<CityResponse, ViewHolder<ItemCityBinding>>(CityDiffUtil()) {

    private lateinit var binding: ItemCityBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ItemCityBinding> {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = ItemCityBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemCityBinding>, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            tvCityName.text = item.name
            tvCountryCode.text = String.format("${item.country} ${getFlagEmoji(item.country)}")
            btnAdd.setImageResource(brnSrc)
            btnAdd.setOnClickListener { addFav(item) }
            btnGoToForward.setOnClickListener { goToForward(item) }
            itemView.setOnClickListener { itemClick(item) }
        }
    }
}

class CityDiffUtil : DiffUtil.ItemCallback<CityResponse>() {
    override fun areItemsTheSame(
        oldItem: CityResponse,
        newItem: CityResponse,
    ): Boolean {
        return (oldItem.longitude == newItem.longitude) && (oldItem.latitude == newItem.latitude)
    }

    override fun areContentsTheSame(
        oldItem: CityResponse,
        newItem: CityResponse,
    ): Boolean {
        return oldItem == newItem
    }
}