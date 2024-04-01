package app.harbi.crysky.ui.home.view.favorite

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.harbi.crysky.R
import app.harbi.crysky.databinding.DialogDeleteBinding
import app.harbi.crysky.databinding.FragmentFavoriteBinding
import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.ui.home.viewmodel.FavoriteViewModel
import app.harbi.crysky.ui.search.view.CityAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var cities: List<CityResponse> = emptyList()
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var locationPreferences: SharedPreferences
    private val viewModel: FavoriteViewModel by viewModel()
    private val cityAdapter: CityAdapter by lazy {
        CityAdapter(R.drawable.ic_delete, {}, { onDeleteClick(it) }, {
            saveSetting(it)
            requireActivity().supportFragmentManager.popBackStack()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationPreferences =
            requireActivity().getSharedPreferences(Constants.LOCATION, Context.MODE_PRIVATE)

        binding.rvResult.adapter = cityAdapter

        lifecycleScope.launch {
            viewModel.cities.collect {
                cityAdapter.submitList(it)
                cities = it
                Log.i("TAG", "Cities loaded successfully: $it")
            }
        }

        binding.etSearchResult.addTextChangedListener {
            val text = it?.trim() ?: ""
            if (text.isNotEmpty()) {
                lifecycleScope.launch {
                    cityAdapter.submitList(
                        cities.asFlow().filter { city -> city.name.contains(text) }.toList()
                    )
                }
            } else {
                cityAdapter.submitList(cities)
            }
        }

        viewModel.getFavLocalCities()
    }

    private fun saveSetting(city: CityResponse) {
        val editor = locationPreferences.edit()
        editor.putString(Constants.LATITUDE, city.latitude.toString())
        editor.putString(Constants.LONGITUDE, city.longitude.toString())
        editor.apply()
    }

    private fun onDeleteClick(city: CityResponse) {
        val dialogView = DialogDeleteBinding.inflate(layoutInflater)

        val alertDialog =
            MaterialAlertDialogBuilder(requireContext()).setView(dialogView.root).setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.gradient_shape_90, requireActivity().theme
                )
            ).create()

        dialogView.apply {

            textView.text = getString(R.string.delete_confirm, city.name)

            btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }

            btnDelete.setOnClickListener {
                viewModel.deleteCity(city)
                viewModel.getFavLocalCities()
                Toast.makeText(requireContext(), "${city.name} Deleted", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }

}