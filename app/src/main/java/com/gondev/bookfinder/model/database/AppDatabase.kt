package com.gondev.bookfinder.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gondev.bookfinder.model.database.dao.BookDAO
import com.gondev.bookfinder.model.database.entity.BookEntity


const val DB_VERSION = 1
internal const val DB_NAME = "book_finder.db"

@Database(
	entities = [
		BookEntity::class,
	],
	version = DB_VERSION,
	exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBookDao(): BookDAO
}