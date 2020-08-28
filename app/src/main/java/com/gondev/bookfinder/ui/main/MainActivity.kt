package com.gondev.bookfinder.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.gondev.bookfinder.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * 메인 엑티비티 입니다
 * 하위항목으로 [ViewPager]를 가지고 있고
 * 이 [ViewPager]에서 [BooksFragment][com.gondev.bookfinder.ui.main.fragments.books.BooksFragment]와
 * [BookShelfFragment][com.gondev.bookfinder.ui.main.fragments.bookshelf.BookShelfFragment]를
 * 표시 합니다
 *
 * @see SectionsPagerAdapter
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.tab_title)[position]
        }.attach()
    }
}