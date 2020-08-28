package com.gondev.bookfinder.model.network.dto

import com.gondev.bookfinder.model.database.entity.BookEntity

data class Result(
	val totalItems: Int,
	val items: List<Book>,
)

data class Book(
	val id: String,
	val volumeInfo: VolumeInfo,
) {
    fun toEntity() = BookEntity(
		id = id,
		title = volumeInfo.title,
		subtitle = volumeInfo.subtitle,
		authors = volumeInfo.authors?.joinToString()?:"",
		publishedDate = volumeInfo.publishedDate,
		description = volumeInfo.description,
		pageCount = volumeInfo.pageCount,
		averageRating = volumeInfo.averageRating,
		ratingsCount = volumeInfo.ratingsCount,
		smallThumbnail = volumeInfo.imageLinks.smallThumbnail,
		thumbnail = volumeInfo.imageLinks.thumbnail,
		language = volumeInfo.language,
	)
}

data class VolumeInfo(
	val title: String,
	val subtitle: String?,
	val authors: List<String>?,
	val publishedDate: String?,
	val description: String?,
	val pageCount: Int,
	val averageRating: Float,
	val ratingsCount: Int,
	val imageLinks: ImageLinks,
	val language: String,
)

data class ImageLinks(
	val smallThumbnail: String,
	val thumbnail: String,
)