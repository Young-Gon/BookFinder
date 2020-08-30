package com.gondev.bookfinder.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//참고: 후행쉼표(https://kotlinlang.org/docs/reference/whatsnew14.html#trailing-comma) 기능 사용으로
// Kotlin 1.4 버전이 필요 합니다
@Entity(tableName = "book")
data class BookEntity (
	@PrimaryKey
	val id: String,
	val title: String,
	val subtitle: String?,
	val authors: String,
	val publishedDate: String?,
	val description: String?,
	val pageCount: Int,
	val averageRating: Float,
	val ratingsCount: Int,
	val smallThumbnail: String?,
	val thumbnail: String?,
	val language: String,
	val isOwned: Boolean = false,
)