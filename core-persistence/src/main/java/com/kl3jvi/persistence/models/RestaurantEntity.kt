package com.kl3jvi.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "table_restaurant")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val status: String,
    @Embedded
    val sortingValues: SortingValuesEntity?,
)