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

class RestaurantAdapter(
    private val onFavoriteClickListener: OnFavoriteButtonClickListener
) : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(RestaurantDiffUtil) {

    inner class RestaurantViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                setClickListener { view ->
                    binding.restaurantInfo?.let { restaurant ->
                        /* Creating a click listener that will navigate to the details fragment. */
                        Navigation.createNavigateOnClickListener(
                            R.id.to_details,
                            bundleOf(
                                "title" to restaurant.name,
                                "restaurantData" to restaurant
                            )
                        ).onClick(view)
                    }
                }
                favoriteClickListener = onFavoriteClickListener
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

    /* Compare two objects. */
    private object RestaurantDiffUtil : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant) =
            oldItem.name == newItem.name &&
                oldItem.sortingValues == newItem.sortingValues &&
                oldItem.status == newItem.status &&
                oldItem.isFavorite == newItem.isFavorite

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant) =
            oldItem == newItem
    }
}
