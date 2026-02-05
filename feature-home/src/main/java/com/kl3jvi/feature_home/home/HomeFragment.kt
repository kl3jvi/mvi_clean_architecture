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
import com.kl3jvi.feature_home.adapter.RestaurantAdapter
import com.kl3jvi.feature_home.databinding.FragmentHomeBinding
import com.kl3jvi.feature_home.util.createFragmentMenu
import com.kl3jvi.feature_home.util.launchAndRepeatWithViewLifecycle
import com.kl3jvi.model.Restaurant
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

/**
 * Home Fragment implementing MVI pattern.
 * - Renders [HomeState] from ViewModel
 * - Emits [HomeIntent] for user actions
 * - Reacts to [HomeEffect] for one-time events
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var restaurantAdapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater).also { binding = it }.run { root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupAdapter()
        setupMenu()
        observeState()
        observeEffects()
    }

    private fun setupAdapter() {
        // Pass lambda for favorite clicks instead of making ViewModel implement an interface
        restaurantAdapter = RestaurantAdapter { restaurant ->
            viewModel.processIntent(HomeIntent.ToggleFavorite(restaurant))
        }

        binding.restaurantRv.apply {
            adapter = restaurantAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupMenu() {
        createFragmentMenu(R.menu.sort_options) {
            when (it.itemId) {
                R.id.sort_action -> {
                    showSortDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showSortDialog() {
        val sortingList = SortOptions.entries.map { it.name.normalized() }.toTypedArray()
        val currentIndex = viewModel.state.value.selectedSortIndex

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.title))
            .setSingleChoiceItems(sortingList, currentIndex) { dialog, which ->
                viewModel.processIntent(
                    HomeIntent.SelectSortOption(SortOptions.entries[which])
                )
                dialog.dismiss()
            }
            .show()
    }

    private fun observeState() {
        launchAndRepeatWithViewLifecycle {
            viewModel.state.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: HomeState) {
        binding.loadingProgress.isVisible = state.isLoading
        binding.restaurantRv.isVisible = !state.isLoading && state.error == null

        if (!state.isLoading && state.error == null) {
            restaurantAdapter.submitList(state.restaurants)
        }
    }

    private fun observeEffects() {
        launchAndRepeatWithViewLifecycle {
            viewModel.effects.collect { effect ->
                handleEffect(effect)
            }
        }
    }

    private fun handleEffect(effect: HomeEffect) {
        when (effect) {
            is HomeEffect.ShowMessage -> {
                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
            }
            is HomeEffect.ShowError -> {
                val message = when (val error = effect.error) {
                    is HomeError.NetworkError -> error.message
                    is HomeError.UnknownError -> error.message
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
            is HomeEffect.NavigateToDetails -> {
                // Future: Navigate to details screen
            }
        }
    }

    private fun String.normalized(): String {
        return this.replace("_", " ").lowercase().replaceFirstChar { firstChar ->
            if (firstChar.isLowerCase()) firstChar.titlecase(Locale.getDefault())
            else firstChar.toString()
        }
    }
}
