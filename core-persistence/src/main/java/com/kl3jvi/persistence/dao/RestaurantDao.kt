package com.kl3jvi.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kl3jvi.persistence.models.RestaurantEntity
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.TestOnly

@Dao
interface RestaurantDao {

    /**
     * This function returns a Flow of a List of RestaurantEntity objects
     */
    @Query("SELECT * FROM table_restaurant")
    fun getFavoriteRestaurants(): Flow<List<RestaurantEntity>?>

    /**
     * Inserts a restaurant into the database
     *
     * @param restaurant RestaurantEntity - This is the RestaurantEntity object that we want to insert
     * into the database.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRestaurant(restaurant: RestaurantEntity): Long?

    /**
     * This function returns the id of a restaurant with a given name
     *
     * @param name The name of the restaurant
     */
    @Query("SELECT id FROM table_restaurant WHERE name = :name")
    suspend fun getRestaurantIdByName(name: String): Long?


    @TestOnly
    @Query("SELECT * FROM table_restaurant WHERE id = :id")
    suspend fun getRestaurantById(id: Long): RestaurantEntity


    /**
     * Delete a restaurant from the database
     *
     * @param id The id of the restaurant to be deleted.
     */
    @Query("DELETE FROM table_restaurant WHERE id =:id")
    suspend fun deleteRestaurant(id: Long)

}