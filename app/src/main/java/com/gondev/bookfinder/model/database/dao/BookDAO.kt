package com.gondev.bookfinder.model.database.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gondev.bookfinder.model.database.entity.BookEntity

@Dao
interface BookDAO {
    @Query("SELECT * FROM book WHERE title like '%' || :query || '%'")
    fun findBooks(query: String): DataSource.Factory<Int, BookEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(items: List<BookEntity>)
}
