package com.kl3jvi.feature_home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kl3jvi.feature_home.R
import com.kl3jvi.feature_home.adapter.OnFavoriteButtonClickListener
import com.kl3jvi.feature_home.adapter.RestaurantAdapter
import com.kl3jvi.feature_home.databinding.FragmentHomeBinding
import com.kl3jvi.feature_home.shared.RestaurantUiState
import com.kl3jvi.feature_home.shared.SharedViewModel
import com.kl3jvi.feature_home.util.createFragmentMenu
import com.kl3jvi.feature_home.util.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: SharedViewModel by viewModels()
    private lateinit var restaurantAdapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding
        .inflate(inflater)
        .also { binding = it }
        .run { root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantAdapter = RestaurantAdapter(viewModel as OnFavoriteButtonClickListener)

        createFragmentMenu(R.menu.sort_options) {
            when (it.itemId) {
                R.id.sort_action -> {
                    val sortingList = sortingValues()
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.title))
                        .setSingleChoiceItemViewModelUpdated(sortingList)
                        .show()

                    true
                }

                else -> false
            }
        }

        binding.restaurantRv.apply {
            adapter = restaurantAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }


        launchAndRepeatWithViewLifecycle {
            viewModel.uiRestaurantState.collect { restaurantDataState ->
                when (restaurantDataState) {

                    is RestaurantUiState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            restaurantDataState.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    RestaurantUiState.Loading -> {
                        showProgressBar(true)
                        binding.restaurantRv.isVisible = false
                    }

                    is RestaurantUiState.Success -> {
                        showProgressBar(false)
                        binding.restaurantRv.isVisible = true
                        restaurantAdapter.submitList(restaurantDataState.restaurantList)
                    }
                }
            }
        }
    }


    private fun showProgressBar(isVisible: Boolean) {
        binding.loadingProgress.isVisible = isVisible
    }

    /**
     * It takes the values of the enum, maps them to a new array of strings, and returns the new array
     *
     * @return An array of strings.
     */
    private fun sortingValues(): Array<String> {
        return SortOptions.values().map { sortOption ->
            sortOption.name.replace("_", " ").lowercase()
                .replaceFirstChar { firstChar ->
                    if (firstChar.isLowerCase())
                        firstChar.titlecase(Locale.getDefault())
                    else firstChar.toString()
                }
        }.toTypedArray()

    }

    private fun MaterialAlertDialogBuilder.setSingleChoiceItemViewModelUpdated(sortingList: Array<String>) = apply {
        launchAndRepeatWithViewLifecycle {
            viewModel.checkedItem.collectLatest { number ->
                setSingleChoiceItems(sortingList, number) { dialog, which ->
                    viewModel.onSortOptionSelected(SortOptions.values()[which])
                    viewModel.checkedItem.value = which
                    dialog.dismiss()
                }
            }
        }
    }

}




