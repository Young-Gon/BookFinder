package com.gondev.bookfinder.di

import com.gondev.bookfinder.ui.main.fragments.books.BooksViewModel
import com.gondev.bookfinder.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * 뷰모델 관련 모듈 등록 변수 입니다
 */
val viewModelModule = module {
	viewModel { BooksViewModel(get(), get()) }
	viewModel { SearchViewModel(get()) }
}