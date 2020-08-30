package com.gondev.bookfinder.ui.search

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import com.gondev.bookfinder.model.database.dao.KeywordDao
import com.gondev.bookfinder.model.database.dao.insertKeyword
import com.gondev.bookfinder.model.database.entity.KeywordEntity
import com.gondev.bookfinder.ui.main.fragments.books.PAGE_SIZE
import com.gondev.bookfinder.util.Event
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * 검색창을 표시하고 이전에 검색한 키워드 목록을 표시한다
 * @see SearchActivity
 */
class SearchViewModel(
    val dao: KeywordDao,
) : ViewModel() {

    /**
     * 검색어 입력 날짜 표시를 위한 날짜포멧
     */
    private val sdf = SimpleDateFormat("yyyy.MM.dd")

    /**
     * editTextSearch에서 입력한 검색어
     * editTextSearch와 양방향 바인딩이 되어 있어 사용자의 입력에 의해 실시간으로 값이 변경된다
     */
    val keyword = MutableLiveData("")

    /**
     * 이전에 입력했던 검색어 목록
     * [keyword]가 변경되면 [Transfomaions][androidx.lifecycle.Transformations]에 의해 자동으로
     * 변경된 검색어를 쿼리해 온다
     */
    val prevKeywords = keyword.switchMap { keyword ->
        LivePagedListBuilder(dao.findKeywords(keyword), PAGE_SIZE)
            .build()
    }

    /**
     * 검색어를 입력 하면 결과를 [BooksFragment][com.gondev.bookfinder.ui.main.fragments.books.BooksFragment]에
     * 돌려주기위한 작업을 activity에 요청
     */
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

    /**
     * 목록에서 이전 검색어 선택
     */
    fun onClickItem(item: KeywordEntity) = viewModelScope.launch {
        dao.insert(item.copy(createAt = Date()))

        requestSetResultAndFinishActivity.value = Event(item.keyword)
    }

    /**
     * 이전 검색한 날짜를 화면에 표시하기 위해 문자열로 바꿔 준다
     */
    fun getCreateDate(date: Date?) = date?.let { sdf.format(it) }

    /**
     * 이전에 검색한 검색어 삭제
     */
    fun deleteKeyword(entity: KeywordEntity) = viewModelScope.launch {
        dao.delete(entity)
    }
}