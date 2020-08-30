package com.gondev.bookfinder.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gondev.bookfinder.R
import com.gondev.bookfinder.ui.main.fragments.books.BooksFragment
import com.gondev.bookfinder.ui.main.fragments.bookshelf.BookShelfFragment


const val SECTION_TRENDING = 0
const val SECTION_FAVORITES = 1

/**
 * [MainActivity]에서 사용하는 [BooksFragment]와
 * [BookShelfFragment]를 관리 합니다
 *
 * @see [BooksFragment]
 * @see [BookShelfFragment]
 */
class SectionsPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int) = when (position) {
        SECTION_TRENDING -> BooksFragment()
        SECTION_FAVORITES -> BookShelfFragment()
        else -> throw IllegalArgumentException("지원하지 않는 페이지 입니다")
    }

    override fun getItemCount() = 2
}