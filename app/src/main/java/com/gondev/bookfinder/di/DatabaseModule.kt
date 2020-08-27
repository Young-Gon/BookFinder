package com.gondev.bookfinder.di

import androidx.room.Room
import com.gondev.bookfinder.model.database.AppDatabase
import com.gondev.bookfinder.model.database.DB_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


/**
 * 룸 데이터베이스 관련 모듈 등록 변수 입니다
 */
val roomModule = module {
	single {
		Room.databaseBuilder(
			androidApplication(),
			AppDatabase::class.java,
			DB_NAME
		).build()
	}

	single {  get<AppDatabase>().getBookDao() }
}