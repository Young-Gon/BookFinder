package com.gondev.bookfinder.model.database.dao

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.room.*
import com.gondev.bookfinder.model.database.entity.KeywordEntity
import java.util.*


@Dao
interface KeywordDao {

    @Query("SELECT * FROM keyword WHERE keyword like '%' || :keyword || '%'")
    fun findKeywords(keyword: String): DataSource.Factory<Int, KeywordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: KeywordEntity)


    @Delete
    suspend fun delete(entity: KeywordEntity)
}

suspend fun KeywordDao.insertKeyword(keyword: String, createAt: Date = Date()) {
    insert(KeywordEntity(-1, keyword, createAt))
}