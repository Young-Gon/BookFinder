package com.gondev.bookfinder.model.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gondev.bookfinder.model.database.converter.TimeConverter
import java.util.*

@TypeConverters(TimeConverter::class)
@Entity(
    tableName = "keyword",
    indices = [Index(value = ["keyword"],
        unique = true)]
)
data class KeywordEntity (
    @PrimaryKey
    val id: Int,
    val keyword: String,
    val createAt: Date,
)