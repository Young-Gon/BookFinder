package com.gondev.bookfinder.ui.main

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.gondev.bookfinder.R
import com.gondev.bookfinder.ui.main.fragments.books.BooksFragment
import com.gondev.bookfinder.ui.main.fragments.bookshelf.BookShelfFragment


const val SECTION_TRENDING = 0
const val SECTION_FAVORITES = 1

/**
 * [MainActivity]에서 사용하는 [Fragment][androidx.fragment.app.Fragment]를 관리 합니다
 *
 * @see [BooksFragment]
 * @see [BookShelfFragment]
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = when (position) {
        SECTION_TRENDING -> BooksFragment()
        SECTION_FAVORITES -> BookShelfFragment()
        else -> throw IllegalArgumentException("지원하지 않는 페이지 입니다")
    }

    override fun getPageTitle(position: Int) =
        context.resources.getStringArray(R.array.tab_title)[position]

    override fun getCount() = 2
}