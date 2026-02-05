package com.kl3jvi.feature_home.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.kl3jvi.feature_home.R
import com.kl3jvi.feature_home.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    
    // Dedicated ViewModel for details - survives config changes + observes repository
    private val viewModel: DetailsViewModel by viewModels()
    
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailsBinding
        .inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize ViewModel with restaurant data from args (only if not already set)
        if (savedInstanceState == null) {
            viewModel.initialize(args.restaurantData)
        }
        
        setupUi()
        observeState()
    }
    
    private fun setupUi() {
        binding.addToFavoriteFab.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }
    
    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    state.restaurant?.let { restaurant ->
                        binding.restaurantInfo = restaurant
                        binding.addToFavoriteFab.isActivated = state.isFavorite
                        binding.description.generateDescription(restaurant)
                    }
                }
            }
        }
    }

    private fun TextView.generateDescription(restaurant: com.kl3jvi.model.Restaurant) {
        text = restaurant.sortingValues?.run {
            getString(R.string.restaurant_description).format(
                ratingAverage.toString(),
                averageProductPrice.toString(),
                deliveryCosts.toString(),
                minCost.toString(),
                getDistanceInKM()
            )
        }.orEmpty()
    }
}
