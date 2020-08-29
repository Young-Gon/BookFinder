package com.gondev.bookfinder.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import com.gondev.bookfinder.model.database.dao.KeywordDao
import com.gondev.bookfinder.model.database.dao.insertKeyword
import com.gondev.bookfinder.model.database.entity.KeywordEntity
import com.gondev.bookfinder.ui.main.fragments.books.PAGE_SIZE
import com.gondev.bookfinder.util.Event
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SearchViewModel(
    val dao: KeywordDao,
) : ViewModel() {

    private val sdf = SimpleDateFormat("yyyy.MM.dd")

    val keyword = MutableLiveData("")

    val prevKeywords = keyword.switchMap { keyword ->
        LivePagedListBuilder(dao.findKeywords(keyword), PAGE_SIZE)
            .build()
    }

    val requestSetResultAndFinishActivity = MutableLiveData<Event<String>>()
    fun onClickSearch(): Boolean {
        val keyword = this.keyword.value
        if (keyword == null || keyword.isEmpty())
            return false

        viewModelScope.launch {
            dao.insertKeyword(keyword)
            requestSetResultAndFinishActivity.value = Event(keyword)
        }
        return true
    }

    fun onClickItem(item: KeywordEntity) = viewModelScope.launch {
        dao.insert(item.copy(createAt = Date()))

        requestSetResultAndFinishActivity.value = Event(item.keyword)
    }

    fun getCreateDate(date: Date?) = date?.let { sdf.format(it) }

    fun deleteKeyword(entity: KeywordEntity) = viewModelScope.launch {
        dao.delete(entity)
    }
}