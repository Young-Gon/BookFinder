package com.gondev.bookfinder.ui.main.fragments.books

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gondev.bookfinder.model.database.dao.BookDAO
import com.gondev.bookfinder.model.database.entity.BookEntity
import com.gondev.bookfinder.model.network.State
import com.gondev.bookfinder.model.network.api.BookAPI
import com.gondev.bookfinder.util.Event
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 한번에 가저올 GIF목록 크기 입니다
 */
const val PAGE_SIZE = 20

class BooksViewModel(
    val dao: BookDAO,
    val api: BookAPI
) : ViewModel() {

    /**
     * 네트워크 상태를 나타냅니다
     */
    val state = MutableLiveData<State>(State.loading())

    val query = MutableLiveData("")

    var offset = 0

    val books = query.switchMap { query ->
        LivePagedListBuilder(dao.findBooks(query), PAGE_SIZE)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<BookEntity>() {
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    loadDataFromNetwork()
                }

                override fun onItemAtEndLoaded(itemAtEnd: BookEntity) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    loadDataFromNetwork()
                }
            })
            .build()
    }

    /**
     * 네트워크로 부터 offset 이후 부터 PAGE_SIZE 만큼 데이터를 가저 옵니다
     */
    fun loadDataFromNetwork() {
        val query = this.query.value
        Timber.i("query=${query}, offset=${offset}")
        if (query == null || query.isEmpty()) {
            state.value = State.success()
            return
        }

        viewModelScope.launch {
            state.value = State.loading()
            try {
                val result = api.requestBooks(
                    query = query,
                    offset = offset,
                    pageSize = PAGE_SIZE
                )

                dao.insert(result.items.map { it.toEntity() })
                offset += result.items.size
                state.value = State.success()
            } catch (e: Exception) {
                Timber.e(e)
                state.value = State.error(e)
            }
        }
    }

    val requestOpenDetailActivity = MutableLiveData<Event<BookEntity>>()
    fun onClickItem(item: BookEntity) {
        requestOpenDetailActivity.value = Event(item)
    }

    val requestOpenSearchActivity = MutableLiveData<Event<Boolean>>()
    fun onClickSearch() {
        requestOpenSearchActivity.value = Event(true)
    }
}