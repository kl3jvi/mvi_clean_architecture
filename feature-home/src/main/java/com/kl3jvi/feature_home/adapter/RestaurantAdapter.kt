package com.kl3jvi.feature_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.feature_home.R
import com.kl3jvi.feature_home.databinding.ItemRestaurantBinding
import com.kl3jvi.model.Restaurant

/**
 * Adapter for displaying restaurants in a RecyclerView.
 *
 * @param onFavoriteClick Lambda called when user taps favorite button
 */
class RestaurantAdapter(
    private val onFavoriteClick: (Restaurant) -> Unit
) : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(RestaurantDiffUtil) {

    inner class RestaurantViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.apply {
                setClickListener { view ->
                    binding.restaurantInfo?.let { restaurant ->
                        Navigation.createNavigateOnClickListener(
                            R.id.to_details,
                            bundleOf(
                                "title" to restaurant.name,
                                "restaurantData" to restaurant
                            )
                        ).onClick(view)
                    }
                }
                // Create functional interface from lambda
                favoriteClickListener = OnFavoriteButtonClickListener { restaurant ->
                    onFavoriteClick(restaurant)
                }
            }
        }

        fun bind(restaurant: Restaurant?) {
            binding.apply {
                restaurantInfo = restaurant
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding =
            ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun getItemCount() = currentList.size

    private object RestaurantDiffUtil : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant) =
            oldItem == newItem &&
                oldItem.isFavorite == newItem.isFavorite &&
                oldItem.status == newItem.status
    }
}
