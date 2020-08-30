package com.gondev.bookfinder.ui.main.fragments.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gondev.bookfinder.model.database.dao.BookDAO
import com.gondev.bookfinder.model.database.entity.BookEntity
import com.gondev.bookfinder.model.network.State
import com.gondev.bookfinder.model.network.api.BookAPI
import com.gondev.bookfinder.ui.main.fragments.bookshelf.BookShelfViewModel
import com.gondev.bookfinder.util.Event
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 한번에 가저올 GIF목록 크기 입니다
 */
const val PAGE_SIZE = 20

/**
 * 도서목록입니다
 * Google Book API를 이용하여 도서 목록을 검색하고 결과를 보여 줍니다
 * 도서 구매/삭제 기능을 위해 [BookShelfViewModel] 클레스를 상속 받고 있습니다
 */
class BooksViewModel(
    dao: BookDAO,
    val api: BookAPI
) : BookShelfViewModel(dao) {

    /**
     * 네트워크 상태를 나타냅니다
     */
    val state = MutableLiveData<State>(State.loading())

    /**
     * 검색어 입니다
     */
    val query = MutableLiveData("")

    /**
     * 도서목록을 페이지 단위로 가저오기위해 어디까지 가저왔는지 나타내느 변수 입니다
     */
    var offset = 0

    /**
     * 도서 목록 입니다
     * [keyword]가 변경되면 [Transfomaions][androidx.lifecycle.Transformations]에 의해 자동으로
     * 변경된 검색어를 쿼리해 옵니다
     */
    override val books = query.switchMap { query ->
        // DataSource.Factory로 부터 livedata를 만듭니다
        // 사용자의 페이징 요청에 따라 디비로 부터 데이터를 읽어 옵니다
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
     * 총 도서 검색 결과 수 입니다
     */
    val result = MutableLiveData("0")

    /**
     * 네트워크로 부터 offset 이후 부터 PAGE_SIZE 만큼 데이터를 가저 옵니다
     * 가저온 데이터는 데이터베이스에 저장합니다
     */
    fun loadDataFromNetwork() {
        val query = this.query.value
        Timber.i("query=${query}, offset=${offset}")
        if (query == null || query.isEmpty()) {
            state.value = State.success()
            return
        }

        this@BooksViewModel.result.value = "0"
        viewModelScope.launch {
            state.value = State.loading()
            try {
                val result = api.requestBooks(
                    query = query,
                    offset = offset,
                    pageSize = PAGE_SIZE
                )

                dao.insert(result.items.map { it.toEntity() })
                this@BooksViewModel.result.value = result.totalItems.toString()
                offset += result.items.size
                state.value = State.success()
            } catch (e: Exception) {
                Timber.e(e)
                state.value = State.error(e)
            }
        }
    }

    /**
     * ViewModel은 View와 Model 사이에 존재한다
     * Model에서 온 data는 ViewModel에서 잠시 있다가
     * UI를 구성하기 위해 View로 흘러간다
     * 반대로, 사용자로 부터 data가 변경 되면 다시
     * ViewModel로 돌아 온다
     * 이 때, 변경된 데이터가 ViewModel에서 처리 불가능한
     * 경우라면 어떻게 할까?
     * 예를 들어, 지금과 같이 새로운 Activity를 실행 시키기 위해
     * Context가 필요한 경우이다
     * 이러한 경우 처리방법은 3가지로 나눌수 있다
     *
     * 1. ViewModel에서 직접 Context를 들고 있다가 처리해주는 방법
     *    ViewModel에서 Context를 들고 있으므로 의존관계가 생긴다
     *    좋은 방법이 아니다
     *
     * 2. Context가 필요한 시점에 주입 받는다
     *    이 경우라면 view로 부터 onClick 이벤트를 발생시킨 view를
     *    받을 수 있고, 이 view에서 Context를 얻을 수 있다
     *    하지만 결국 ViewModel 내부에서 context에 접근 한다는 점에서
     *    1번 과 같다 나쁘진 않지만 좋지도 않다
     *
     * 3. Context가 필요한 시점에 이를 처리 할 수 있는 Context로
     *    처리를 위임할 수 있다
     *    이렇게 하면 ViewModel 내부에서는 Context에 접근할 필요가
     *    없고, 의존 관계도 생기지 않는다
     *    결국, 일의 책임 소재가 명확해 진다
     *    좋은 방법이지만 이렇게 하기 위해 결국 [Event]클레스의 도움을
     *    받아야 한다 로직이 복잡해진다
     */
    val requestOpenSearchActivity = MutableLiveData<Event<Boolean>>()
    fun onClickSearch() {
        requestOpenSearchActivity.value = Event(true)
    }
}