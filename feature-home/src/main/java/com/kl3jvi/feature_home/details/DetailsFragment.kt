package com.kl3jvi.feature_home.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.kl3jvi.feature_home.R
import com.kl3jvi.feature_home.databinding.FragmentDetailsBinding
import com.kl3jvi.feature_home.shared.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: SharedViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private val restaurantData get() = args.restaurantData

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
        binding.restaurantInfo = restaurantData
        binding.description.generateDescription()

        /* Setting the onClickListener for the FloatingActionButton. */
        binding.addToFavoriteFab.setOnClickListener {
            viewModel.toggleRestaurantFavoriteState(restaurantData)
            binding.addToFavoriteFab.isActivated = !binding.addToFavoriteFab.isActivated
        }
    }

    private fun TextView.generateDescription() {
        text = restaurantData.sortingValues?.run {
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
